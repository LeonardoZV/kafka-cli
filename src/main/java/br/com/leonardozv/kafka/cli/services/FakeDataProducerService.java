package br.com.leonardozv.kafka.cli.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FakeDataProducerService {

	private final GenericKafkaProducerService genericKafkaProducerService;

	public FakeDataProducerService(GenericKafkaProducerService genericKafkaProducerService)  {
		this.genericKafkaProducerService = genericKafkaProducerService;
	}

    public void generateAndProduceEvents(String topic, Schema schema, String tokenizedKey, String tokenizedHeader, String tokenizedValue, Integer batches, Long eventsPerBatch) throws IOException {

    	ObjectMapper mapper = new ObjectMapper();

		for (int b = 1; b <= batches; b++) {

			for (long e = 1; e <= eventsPerBatch; e++) {

				Headers headers = new RecordHeaders();

				if (tokenizedHeader != null) {
					JsonNode headersJsonNode = mapper.readTree(replaceTokens(tokenizedHeader));
					headersJsonNode.fields().forEachRemaining(h -> headers.add(h.getKey(), h.getValue().asText().getBytes()));
				}

				String key = null;

				if (tokenizedKey != null) {
					key = replaceTokens(tokenizedKey);
				}

				String value = null;

				if (tokenizedValue != null) {
					value = replaceTokens(tokenizedValue);
				}

				this.genericKafkaProducerService.produce(topic, schema, key, headers, value);

			}

			this.genericKafkaProducerService.flush();

		}

    }
    
    private String replaceTokens(String tokenizedString) {

		return tokenizedString
				.replace("{UUID}", UUID.randomUUID().toString())
				.replace("{DATE-FORMATO-ISO}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
				.replace("{DATE-FORMATO-YYYYMMDD}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.replace("{DATETIME-FORMATO-ISO}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }
    
}
