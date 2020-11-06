package br.com.itau.kafka.cli.services;

import java.util.List;

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

import br.com.itau.kafka.cli.config.AppConfiguration;
import br.com.itau.kafka.cli.mappers.CloudEventsMessageHeaderMapper;
import br.com.itau.kafka.cli.models.CloudEventsMessageHeader;
import io.confluent.kafka.serializers.GenericContainerWithVersion;

@Service
public class KafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
	
	@Autowired
	private AppConfiguration appConfiguration;
	
	@Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
	
	public String[] obterTopicos() {
		return this.appConfiguration.getTopics();
	}
	
	public String obterGroupId() {
		return this.appConfiguration.getGroupId();
	}
	
	@KafkaListener(id = "itau-kafka-cli-java", autoStartup = "false", containerFactory = "kafkaListenerContainerFactory", topics = "#{kafkaConsumerService.obterTopicos()}", groupId = "#{kafkaConsumerService.obterGroupId()}", idIsGroup = false)
	private void consumir(List<Message<GenericContainerWithVersion>> listaEventos, Acknowledgment ack) throws Exception {		

		ObjectMapper objectMapper = new ObjectMapper();
		
		for(Message<GenericContainerWithVersion> evento : listaEventos) {	
			
			CloudEventsMessageHeader header = CloudEventsMessageHeaderMapper.from(evento.getHeaders());
						
			log.info("Headers: " + objectMapper.writeValueAsString(header) + " | Payload: " + evento.getPayload().container().toString());
			
		}
		
		if (this.appConfiguration.getCommit()) {
			ack.acknowledge();
		};

		log.info("Batch Eventos Consumidos");
		
	}
	
    public void start() {
    	
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("itau-kafka-cli-java");

        listenerContainer.start();

    }
	
}
