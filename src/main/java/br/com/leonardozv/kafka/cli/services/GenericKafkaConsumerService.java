package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.models.CloudEventsMessageHeader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.confluent.kafka.serializers.GenericContainerWithVersion;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaderMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericKafkaConsumerService {
	
	private static final Logger log = LoggerFactory.getLogger(GenericKafkaConsumerService.class);

	private final Boolean commit;

	private final KafkaHeaderMapper kafkaHeaderMapper = new DefaultKafkaHeaderMapper();

	private final ObjectMapper objectMapper = new ObjectMapper();

	public GenericKafkaConsumerService(Boolean commit) {
		this.commit = commit;
		this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public void onMessage(List<ConsumerRecord<String, GenericContainerWithVersion>> messages, Acknowledgment ack) {

		int i = 0;

		for(ConsumerRecord<String, GenericContainerWithVersion> message : messages) {

			try {

				Map<String, Object> map = new HashMap<>();

				this.kafkaHeaderMapper.toHeaders(message.headers(), map);

				CloudEventsMessageHeader header = this.objectMapper.convertValue(map, CloudEventsMessageHeader.class);

				if (log.isInfoEnabled()) {
					log.info("Headers: {} | Payload: {}", this.objectMapper.writeValueAsString(header), message.value().container());
				}

			} catch(Exception ex) {
				throw new BatchListenerFailedException("Error in message consumption", ex, i);
			}

			i++;

		}

		if (Boolean.TRUE.equals(commit)) {
			ack.acknowledge();
		}

	}
	
}
