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

	private final KafkaTemplate<Record, Record> kafkaTemplate;

	private final DecoderFactory decoderFactory = new DecoderFactory();

	public GenericKafkaProducerService(KafkaTemplate<Record, Record> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
		
	public CompletableFuture<SendResult<Record, Record>> produce(String topic, Headers headers, String key, Schema keySchema, String value, Schema valueSchema) throws IOException {

		Record keyGenericRecord;

		if (key == null) {
			keyGenericRecord = null;
		} else {
			Decoder decoder = decoderFactory.jsonDecoder(keySchema, key);
			DatumReader<Record> reader = new GenericDatumReader<>(keySchema);
			keyGenericRecord = reader.read(null, decoder);
		}

		Record valueGenericRecord;

		if (value == null) {
			valueGenericRecord = null;
		} else {
			Decoder decoder = decoderFactory.jsonDecoder(valueSchema, value);
			DatumReader<Record> reader = new GenericDatumReader<>(valueSchema);
			valueGenericRecord = reader.read(null, decoder);
		}

		ProducerRecord<Record, Record> producerRecord = new ProducerRecord<>(topic, null, null, keyGenericRecord, valueGenericRecord, headers);

		return this.kafkaTemplate.send(producerRecord).completable();
		
	}

	public void flush() {
		this.kafkaTemplate.flush();
	}

}
