package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FakeDataProducerService extends KafkaProducerService {

	private final AppConfiguration appConfiguration;

	public FakeDataProducerService(AppConfiguration appConfiguration, KafkaTemplate<String, GenericData.Record> kafkaTemplate)  {
		super(kafkaTemplate);
		this.appConfiguration = appConfiguration;
	}

    public void generateAndProduceEvents(String topic, Schema schema, String tokenizedKey, String tokenizedHeader, String tokenizedValue) throws IOException, InterruptedException {

    	ObjectMapper mapper = new ObjectMapper();

		for (int b = 1; b <= this.appConfiguration.getBatches(); b++) {

			for (long e = 1; e <= this.appConfiguration.getEvents(); e++) {

				JsonNode headerJsonNode = null;

				if (tokenizedHeader != null) {
					headerJsonNode = mapper.readTree(replaceTokens(tokenizedHeader));
				}

				String key = null;

				if (tokenizedKey != null) {
					key = replaceTokens(tokenizedKey);
				}

				String value = null;

				if (tokenizedValue != null) {
					value = replaceTokens(tokenizedValue);
				}

				this.produce(topic, schema, key, headerJsonNode, value);

			}

			this.kafkaTemplate.flush();

		}

    }
    
    public String replaceTokens(String jsonString) {

		return jsonString
				.replace("{UUID}", UUID.randomUUID().toString())
				.replace("{DATE-FORMATO-ISO}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
				.replace("{DATE-FORMATO-YYYYMMDD}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.replace("{DATETIME-FORMATO-ISO}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }
    
}
