package org.example.iotgreenhouse.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.example.iotgreenhouse.controller.SensorDataRequest;
import org.example.iotgreenhouse.model.TemperatureSensor;
import org.example.iotgreenhouse.repository.SensorsRepository;
import org.example.iotgreenhouse.service.GreenHouseManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.example.iotgreenhouse.model.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class MqttConfig {
    private final GreenHouseManagementService service;
    private final SensorsRepository sensorsRepository;
    private final ObjectMapper mapper;

    private String URL = "tcp://localhost:1883";
    private String CLIENT_ID = "spring-boot-iot-client";
    private String TOPIC = "greenhouse/sensors";

    public MqttConfig(GreenHouseManagementService service, SensorsRepository sensorsRepository, ObjectMapper mapper) {
        this.service = service;
        this.sensorsRepository = sensorsRepository;
        this.mapper = mapper;
    }

    @Bean
    public MqttConnectOptions mqttConnectionOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] {"tcp://localhost:1883"});
        options.setAutomaticReconnect(false);
        options.setCleanSession(true);

        return options;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean MessageChannel mqttOutputChannel() { return new DirectChannel(); }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                URL, CLIENT_ID, TOPIC);
        adapter.setCompletionTimeout(5000);
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String payload = (String) message.getPayload();
            // Закомментируем этот вывод, чтобы консоль не засорялась, мы будем видеть логи в Visitor
            // System.out.println("[MQTT] Получено сообщение: " + payload);

            try {
                SensorDataRequest request = mapper.readValue(payload, SensorDataRequest.class);

                if ("TEMPERATURE".equalsIgnoreCase(request.getType())) {
                    TemperatureSensor temperatureSensor = (TemperatureSensor) sensorsRepository.findById(request.getId()).orElse(null);

                    if (temperatureSensor != null) {
                        temperatureSensor.setVal(request.getVal());
                        service.handleIncomingSensorData(temperatureSensor);
                        sensorsRepository.save(temperatureSensor);
                    }
                }
                else if ("HUMIDITY".equalsIgnoreCase(request.getType())) {
                    HumiditySensor humiditySensor = (HumiditySensor) sensorsRepository.findById(request.getId()).orElse(null);

                    if (humiditySensor != null) {
                        humiditySensor.setVal(request.getVal());
                        service.handleIncomingSensorData(humiditySensor);
                        sensorsRepository.save(humiditySensor);
                    }
                }
                else if ("LIGHT".equalsIgnoreCase(request.getType())) {
                    LightSensor lightSensor = (LightSensor) sensorsRepository.findById(request.getId()).orElse(null);

                    if (lightSensor != null) {
                        // Приводим double к int, так как яркость у нас в целых процентах
                        lightSensor.setVal(request.getVal());
                        service.handleIncomingSensorData(lightSensor);
                        sensorsRepository.save(lightSensor);
                    }
                }
            } catch (Exception e) {
                System.out.println("[MQTT] Ошибка парсинга: " + e.getMessage());
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MessageHandler mqttOutbound() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectionOptions());

        String PUBLISHER_CLIENT_ID = CLIENT_ID + "-publisher";

        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                PUBLISHER_CLIENT_ID, factory
        );
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }
}
