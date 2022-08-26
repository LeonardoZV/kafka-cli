package br.com.leonardozv.kafka.cli.serializers;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerializer;
import io.confluent.kafka.serializers.AvroSchemaUtils;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CustomKafkaAvroSerializer extends AbstractKafkaAvroSerializer implements Serializer<Object> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		configure(new KafkaAvroSerializerConfig(configs));
	}

	@Override
	public byte[] serialize(String topic, Object message) {
		return serializeImpl(getSubjectName(topic, false, message, AvroSchemaUtils.getSchema(message)), message);
	}
}