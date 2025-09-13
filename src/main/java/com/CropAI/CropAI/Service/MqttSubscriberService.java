package com.CropAI.CropAI.Service;

//package com.CropAI.CropAI.Service;

import com.CropAI.CropAI.Entity.User;
import com.CropAI.CropAI.Repositories.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class MqttSubscriberService {

    private final SimpMessagingTemplate websocketTemplate;
    private final UserRepository userRepository;
    private final MqttPahoClientFactory mqttClientFactory;

    private final Map<String, MqttPahoMessageDrivenChannelAdapter> subscriptions = new ConcurrentHashMap<>();

    @Autowired
    public MqttSubscriberService(SimpMessagingTemplate websocketTemplate,
                                 UserRepository userRepository,
                                 MqttPahoClientFactory mqttClientFactory) {
        this.websocketTemplate = websocketTemplate;
        this.userRepository = userRepository;
        this.mqttClientFactory = mqttClientFactory;
    }

    // Create a new channel for MQTT messages
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    // Subscribe user to multiple topics dynamically
    public void subscribeUserToTopics(String userId, List<String> topics) {
        for (String topic : topics) {
            if (!subscriptions.containsKey(topic)) {
                MqttPahoMessageDrivenChannelAdapter adapter =
                        new MqttPahoMessageDrivenChannelAdapter(
                                "springBackend_" + topic,
                                mqttClientFactory,
                                topic
                        );
                adapter.setOutputChannel(mqttInputChannel());
                adapter.start(); // start listening
                subscriptions.put(topic, adapter);
            }
        }
    }

    // Handles incoming MQTT messages from all topics
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<?> message) {
        try {
            String payload = (String) message.getPayload();
            JSONObject json = new JSONObject(payload);

            // Extract API key from payload
            String apiKey = json.getString("apiKey");

            // Find the user by device API key
            User user = userRepository.findUserByDeviceApiKey(apiKey);
            if (user == null) {
                System.out.println("Unauthorized device tried to publish: " + apiKey);
                return;
            }

            // Forward payload to mobile app via WebSocket
            String userId = user.getId();
            String topic = (String) message.getHeaders().get("mqtt_receivedTopic"); // original MQTT topic
            websocketTemplate.convertAndSend("/topic/" + userId + "/" + topic, json.toString());

        } catch (Exception e) {
            System.out.println("Error handling MQTT message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
