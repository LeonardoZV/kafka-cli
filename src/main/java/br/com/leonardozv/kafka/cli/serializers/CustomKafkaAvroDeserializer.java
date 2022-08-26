package br.com.leonardozv.kafka.cli.serializers;

import io.confluent.kafka.serializers.AbstractKafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CustomKafkaAvroDeserializer extends AbstractKafkaAvroDeserializer implements Deserializer<Object> {
	
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		configure(new KafkaAvroDeserializerConfig(configs));
	}
	
	@Override
	public Object deserialize(String s, byte[] bytes) {
		return deserializeWithSchemaAndVersion(s, false, bytes);
	}

}