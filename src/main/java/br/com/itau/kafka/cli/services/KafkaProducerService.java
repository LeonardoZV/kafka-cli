package br.com.itau.kafka.cli.services;

import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
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
	
	@Autowired
	private KafkaTemplate<String, GenericData.Record> kafkaTemplate;
	
	public CompletableFuture<SendResult<String, Record>> produzir(String topico, Schema schema, JsonNode headerJson, JsonNode payloadJson) throws Exception {
		
		DecoderFactory decoderFactory = new DecoderFactory();		
		Decoder decoder = decoderFactory.jsonDecoder(schema, payloadJson.toString());		
		DatumReader<GenericData.Record> reader = new GenericDatumReader<GenericData.Record>(schema);		
		GenericData.Record genericRecord = reader.read(null, decoder);

		ProducerRecord<String, GenericData.Record> record = new ProducerRecord<String, GenericData.Record>(topico, genericRecord);
		
		if (headerJson != null) {
			
			headerJson.fields().forEachRemaining(h -> {
				record.headers().add(h.getKey(), h.getValue().asText().getBytes());
			});
			
		}

		return kafkaTemplate.send(record).completable();
		
	}
	
	public void printarMetricas() {

		for (Entry<MetricName, ? extends Metric> entry : kafkaTemplate.metrics().entrySet()) {
			log.info(entry.getKey().name() + " : " + entry.getValue().metricValue());
		}
		
	}

}
