package br.com.leonardozv.kafka.cli.services;

import java.util.List;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import br.com.leonardozv.kafka.cli.models.CloudEventsMessageHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.GenericContainerWithVersion;

import br.com.leonardozv.kafka.cli.mappers.CloudEventsMessageHeaderMapper;

@Service
public class KafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

	private final AppConfiguration appConfiguration;

	private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	@Autowired
	public KafkaConsumerService(AppConfiguration appConfiguration, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
		this.appConfiguration = appConfiguration;
		this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
	}

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public String[] obterTopicos() {
		return this.appConfiguration.getTopics();
	}
	
	public String obterGroupId() {
		return this.appConfiguration.getGroupId();
	}
	
	@KafkaListener(id = "kafka-cli-java", autoStartup = "false", containerFactory = "kafkaListenerContainerFactory", topics = "#{kafkaConsumerService.obterTopicos()}", groupId = "#{kafkaConsumerService.obterGroupId()}", idIsGroup = false)
	private void consumir(List<Message<GenericContainerWithVersion>> listaEventos, Acknowledgment ack) throws JsonProcessingException {
		
		for(Message<GenericContainerWithVersion> evento : listaEventos) {	
			
			CloudEventsMessageHeader header = CloudEventsMessageHeaderMapper.from(evento.getHeaders());

			log.info("Headers: {} | Payload: {}", objectMapper.writeValueAsString(header), evento.getPayload().container());

		}
		
		if (Boolean.TRUE.equals(this.appConfiguration.getCommit())) {
			ack.acknowledge();
		}
		
	}
	
    public void start() {
    	
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("kafka-cli-java");

        listenerContainer.start();

    }
	
}
