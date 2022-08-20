package br.com.leonardozv.kafka.cli.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class KafkaProducerService {

	private final KafkaTemplate<String, Record> kafkaTemplate;

	@Autowired
	public KafkaProducerService(KafkaTemplate<String, Record> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	private final DecoderFactory decoderFactory = new DecoderFactory();
		
	public CompletableFuture<SendResult<String, Record>> produzir(String topico, Schema schema, JsonNode headerJson, String key, String value) throws IOException {
				
		Decoder decoder = decoderFactory.jsonDecoder(schema, value);
		DatumReader<Record> reader = new GenericDatumReader<>(schema);
		Record genericRecord = reader.read(null, decoder);
		ProducerRecord<String, Record> producerRecord = new ProducerRecord<>(topico, key, genericRecord);
		
		if (headerJson != null) {
			headerJson.fields().forEachRemaining(h -> producerRecord.headers().add(h.getKey(), h.getValue().asText().getBytes()));
		}

		return this.kafkaTemplate.send(producerRecord).completable();
		
	}
	
	public void flush() {
		
		this.kafkaTemplate.flush();
		
	}

}
