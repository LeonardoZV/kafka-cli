package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import br.com.leonardozv.kafka.cli.models.CompleteMessageHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.confluent.kafka.serializers.GenericContainerWithVersion;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final AppConfiguration appConfiguration;

	private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

	public KafkaConsumerService(AppConfiguration appConfiguration, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
		this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.appConfiguration = appConfiguration;
		this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
	}

	@KafkaListener(topics = "#{appConfiguration.getTopics()}", groupId = "#{appConfiguration.getGroupId()}", autoStartup = "false", batch = "true")
	public void consume(List<Message<GenericContainerWithVersion>> listaEventos, Acknowledgment ack) throws JsonProcessingException {

		for(Message<GenericContainerWithVersion> evento : listaEventos) {

			CompleteMessageHeader header = this.objectMapper.convertValue(evento.getHeaders(), CompleteMessageHeader.class);

			if (log.isInfoEnabled()) {
				log.info("Headers: {} | Payload: {}", this.objectMapper.writeValueAsString(header), evento.getPayload().container());
			}

		}
		
		if (Boolean.TRUE.equals(this.appConfiguration.getCommit())) {
			ack.acknowledge();
		}
		
	}
	
    public void start() {

		MessageListenerContainer listenerContainer = this.kafkaListenerEndpointRegistry.getListenerContainer(this.appConfiguration.getGroupId());

		if (listenerContainer != null) {
			listenerContainer.start();
		}

    }
	
}
