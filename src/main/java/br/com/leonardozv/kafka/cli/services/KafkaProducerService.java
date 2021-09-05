package br.com.leonardozv.kafka.cli.services;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class KafkaProducerService {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

	private final KafkaTemplate<String, Record> kafkaTemplate;

	@Autowired
	public KafkaProducerService(KafkaTemplate<String, Record> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	private final DecoderFactory decoderFactory = new DecoderFactory();
		
	public CompletableFuture<SendResult<String, Record>> produzir(String topico, Schema schema, JsonNode headerJson, String key, String payload) throws Exception {
				
		Decoder decoder = decoderFactory.jsonDecoder(schema, payload);		
		DatumReader<Record> reader = new GenericDatumReader<>(schema);
		Record genericRecord = reader.read(null, decoder);
		ProducerRecord<String, Record> record = new ProducerRecord<>(topico, key, genericRecord);
		
		if (headerJson != null) {
			headerJson.fields().forEachRemaining(h -> record.headers().add(h.getKey(), h.getValue().asText().getBytes()));
		}

		return this.kafkaTemplate.send(record).completable();
		
	}
	
	public void printarMetricas() {

		for (Entry<MetricName, ? extends Metric> entry : kafkaTemplate.metrics().entrySet()) {
			log.info(entry.getKey().group() + " - " + entry.getKey().name() + " : " + entry.getValue().metricValue());
		}
		
	}
	
	public void flush() {
		
		this.kafkaTemplate.flush();
		
	}

}
