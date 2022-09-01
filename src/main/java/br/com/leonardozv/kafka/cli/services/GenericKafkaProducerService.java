package br.com.leonardozv.kafka.cli.services;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class GenericKafkaProducerService {

	private final KafkaTemplate<String, Record> kafkaTemplate;

	private final DecoderFactory decoderFactory = new DecoderFactory();

	public GenericKafkaProducerService(KafkaTemplate<String, Record> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
		
	public CompletableFuture<SendResult<String, Record>> produce(String topic, Schema schema, String key, Headers headers, String value) throws IOException {
				
		Decoder decoder = decoderFactory.jsonDecoder(schema, value);

		DatumReader<Record> reader = new GenericDatumReader<>(schema);

		Record genericRecord = reader.read(null, decoder);

		ProducerRecord<String, Record> producerRecord = new ProducerRecord<>(topic, null, null, key, genericRecord, headers);

		return this.kafkaTemplate.send(producerRecord).completable();
		
	}

	public void flush() {
		this.kafkaTemplate.flush();
	}

}
