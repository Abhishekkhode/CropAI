package com.CropAI.CropAI.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MqttConfig {

    private final String brokerUrl = "tcp://broker.hivemq.com:1883";
    private final String clientId = "springBackend";

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        return new DefaultMqttPahoClientFactory();
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        String[] topics = {"crop/data"}; // + subscribes to all users
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory(), topics);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }
}
