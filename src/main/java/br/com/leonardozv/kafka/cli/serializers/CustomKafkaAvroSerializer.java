package br.com.leonardozv.kafka.cli.serializers;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerializer;
import io.confluent.kafka.serializers.AvroSchemaUtils;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

public class CustomKafkaAvroSerializer extends AbstractKafkaAvroSerializer implements Serializer<Object> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
				
		configure(new KafkaAvroSerializerConfig(configs));
		
	}

	@Override
	public byte[] serialize(String topic, Object record) {
		
		return serializeImpl(getSubjectName(topic, false, record, AvroSchemaUtils.getSchema(record)), record);
		
	}
}