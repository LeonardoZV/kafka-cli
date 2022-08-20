package br.com.leonardozv.kafka.cli.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericData.Record;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import io.confluent.kafka.serializers.GenericContainerWithVersion;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

@Configuration
@EnableKafka
public class KafkaConfiguration {
		
	@Value("${kafka.bootstrap-servers}")
	private String kafkaBootstrapServers;
	
	@Value("${kafka.security-protocol}")
	private String kafkaSecurityProtocol;
	
	@Value("${kafka.sasl-jaas-config}")
	private String saslJaasConfig;
	
	@Value("${kafka.sasl-mechanism}")
	private String saslMechanism;
		
	@Value("${kafka.ssl-keystore-location}")
	private String kafkaSslKeystoreLocation;
	
	@Value("${kafka.ssl-keystore-password}")
	private String kafkaSslKeystorePassword;
	
	@Value("${kafka.ssl-truststore-location}")
	private String kafkaSslTruststoreLocation;
	
	@Value("${kafka.ssl-truststore-password}")
	private String kafkaSslTruststorePassword;
	
	@Value("${schema.registry.url}")
	private String schemaRegistryUrl;
	
	@Value("${schema.registry.basic.auth.credentials.source}")
	private String schemaRegistryBasicAuthCredentialsSource;
	
	@Value("${schema.registry.basic.auth.user.info}")
	private String schemaRegistryBasicAuthUserInfo;
		
	@Value("${kafka.consumer.enable-auto-commit}")
	private boolean kafkaEnableAutoCommit;
	
	@Value("${kafka.consumer.auto-offset-reset}")
	private String kafkaAutoOffsetReset;
	
	@Value("${kafka.consumer.allow-auto-create-topics}")
	private boolean kafakAllowAutoCreateTopics;
	
	@Value("${kafka.consumer.partition-assignment-strategy}")
	private String kafkaPartitionAssignmentStrategy;
	
	@Value("${kafka.consumer.concurrency}")
	private int kafkaConcurrency;
	
	@Value("${kafka.consumer.max-poll-interval-ms}")
	private String kafkaMaxPollIntervalMs;
	
	@Value("${kafka.consumer.max-poll-records}")
	private String kafkaMaxPollRecords;

	@Value("${kafka.consumer.receive-buffer-bytes}")
	private int kafkaReceiveBufferBytes;
	
	@Value("${kafka.consumer.key-deserializer}")
	private String kafkaKeyDeserializer;
	
	@Value("${kafka.consumer.value-deserializer}")
	private String kafakaValueDeserializer;
	
	@Value("${schema.registry.consumer.specific-avro-reader}")
	private boolean schemaRegistryspecificAvroReader;
		
	@Value("${kafka.producer.acks}")
	private String kafakAcks;
	
	@Value("${kafka.producer.enable-idempotence}")
	private Boolean kafkaEnableIdempotence;
	
	@Value("${kafka.producer.batch-size}")
	private int kafkaBatchSize;
	
	@Value("${kafka.producer.linger-ms}")
	private int kafkaLingerMs;

	@Value("${kafka.producer.max-request-size}")
	private int kafkaMaxRequestSize;

	@Value("${kafka.producer.buffer-memory}")
	private int kafkaBufferMemory;

	@Value("${kafka.producer.compression-type}")
	private String kafkaCompressionType;	
	
	@Value("${kafka.producer.key-serializer}")
	private String kafkaKeySerializer;
	
	@Value("${kafka.producer.value-serializer}")
	private String kafakaValueSerializer;
	
	@Value("${schema.registry.producer.auto-register-schemas}")
	private boolean schemaRegistryAutoRegisterSchemas;
	
	@Value("${schema.registry.producer.value-subject-name-strategy}")
	private String schemaRegistryValueSubjectNameStrategy;
	
	@Bean
    public Map<String, Object> consumerConfigs() {
		
		Map<String, Object> props = new HashMap<>();
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
		props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
		props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
//		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaSslKeystoreLocation);
//		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaSslKeystorePassword);
//		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaSslTruststoreLocation);
//		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaSslTruststorePassword);		
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaEnableAutoCommit);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaAutoOffsetReset);
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, kafakAllowAutoCreateTopics);
		props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, kafkaPartitionAssignmentStrategy);
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaMaxPollIntervalMs);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaMaxPollRecords);
		props.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, kafkaReceiveBufferBytes);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaKeyDeserializer);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
		props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, kafakaValueDeserializer);
		
		props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
		props.put(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, schemaRegistryBasicAuthCredentialsSource);
		props.put(KafkaAvroDeserializerConfig.USER_INFO_CONFIG, schemaRegistryBasicAuthUserInfo);
//		props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, schemaRegistryspecificAvroReader);

        return props;

    }
	
	@Bean
    public ConsumerFactory<String, GenericContainerWithVersion> consumerFactory() {
		
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        
    }
	
	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, GenericContainerWithVersion> kafkaListenerContainerFactory() {
		
    	ConcurrentKafkaListenerContainerFactory<String, GenericContainerWithVersion> factory = new ConcurrentKafkaListenerContainerFactory<>();       

        factory.setConsumerFactory(consumerFactory());
        
        factory.setConcurrency(kafkaConcurrency);

        factory.setBatchListener(true);
        
        factory.getContainerProperties().setAckMode(AckMode.MANUAL);
        
        return factory;
        
    }
	
	@Bean
    public Map<String, Object> producerConfigs() {
		
		Map<String, Object> props = new HashMap<>();
		
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
		props.put(SaslConfigs.SASL_JAAS_CONFIG, saslJaasConfig);
		props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
//		props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, kafkaSslKeystoreLocation);
//		props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, kafkaSslKeystorePassword);
//		props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, kafkaSslTruststoreLocation);
//		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, kafkaSslTruststorePassword);		
		props.put(ProducerConfig.ACKS_CONFIG, kafakAcks);
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaEnableIdempotence);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaBatchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaLingerMs);
		props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, kafkaMaxRequestSize);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaBufferMemory);
		props.put(ProducerConfig.SEND_BUFFER_CONFIG, -1);
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaCompressionType);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaKeySerializer);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafakaValueSerializer);
		
		props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
		props.put(KafkaAvroSerializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, schemaRegistryBasicAuthCredentialsSource);
		props.put(KafkaAvroSerializerConfig.USER_INFO_CONFIG, schemaRegistryBasicAuthUserInfo);
		props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, schemaRegistryAutoRegisterSchemas);
//		props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, schemaRegistryValueSubjectNameStrategy);W

        return props;
    }
		
	@Bean
    public ProducerFactory<String, Record> producerFactory() {
		
        return new DefaultKafkaProducerFactory<>(producerConfigs());
        
    }

	@Bean
	public KafkaTemplate<String, Record> kafkaTemplate() {
		
		return new KafkaTemplate<>(producerFactory());
	    
	}
	
}
