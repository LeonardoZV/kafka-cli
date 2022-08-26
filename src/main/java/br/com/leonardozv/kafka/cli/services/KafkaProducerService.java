package br.com.leonardozv.kafka.cli.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class KafkaProducerService {

	protected final KafkaTemplate<String, Record> kafkaTemplate;

	private final DecoderFactory decoderFactory = new DecoderFactory();

	public KafkaProducerService(KafkaTemplate<String, Record> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
		
	public CompletableFuture<SendResult<String, Record>> produce(String topico, Schema schema, String key, JsonNode headerJson, String value) throws IOException {
				
		Decoder decoder = decoderFactory.jsonDecoder(schema, value);
		DatumReader<Record> reader = new GenericDatumReader<>(schema);
		Record genericRecord = reader.read(null, decoder);
		ProducerRecord<String, Record> producerRecord = new ProducerRecord<>(topico, key, genericRecord);
		
		if (headerJson != null) {
			headerJson.fields().forEachRemaining(h -> producerRecord.headers().add(h.getKey(), h.getValue().asText().getBytes()));
		}

		return this.kafkaTemplate.send(producerRecord).completable();
		
	}

}
