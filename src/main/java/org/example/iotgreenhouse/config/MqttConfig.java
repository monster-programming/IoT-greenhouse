package org.example.iotgreenhouse.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.example.iotgreenhouse.controller.SensorDataRequest;
import org.example.iotgreenhouse.model.TemperatureSensor;
import org.example.iotgreenhouse.repository.SensorsRepository;
import org.example.iotgreenhouse.service.GreenHouseManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class MqttConfig {
    private final GreenHouseManagementService service;
    private final SensorsRepository sensorsRepository;
    private final ObjectMapper mapper;

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

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "tcp://localhost:1883", "spring-boot-iot-client", "greenhouse/sensors");
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
            System.out.println("[MQTT] Получено сообщение: " + payload);

            try {
                SensorDataRequest request = mapper.readValue(payload, SensorDataRequest.class);

                if ("TEMPERATURE".equalsIgnoreCase(request.getType())) {
                    TemperatureSensor temperatureSensor = (TemperatureSensor) sensorsRepository.findById(request.getId()).orElse(null);

                    if (temperatureSensor != null) {
                        temperatureSensor.setVal(request.getVal());
                        service.handleIncomingSensorData(temperatureSensor);
                        sensorsRepository.save(temperatureSensor);
                        System.out.println("[MQTT] Данные обновлены");
                    } else {
                        System.out.println("[MQTT] Неизвестный датчик: " + request.getId());
                    }
                }
            } catch (Exception e) {
                System.out.println("[MQTT] Ошибка парсинга: " + e.getMessage());
            }
        };
    }
}
