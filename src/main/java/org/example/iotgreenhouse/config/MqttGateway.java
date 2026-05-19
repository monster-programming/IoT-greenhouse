package org.example.iotgreenhouse.config;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutputChannel")
public interface MqttGateway {

    void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
}
