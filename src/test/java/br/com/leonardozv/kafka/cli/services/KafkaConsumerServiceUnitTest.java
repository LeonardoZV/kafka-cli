package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.confluent.kafka.serializers.GenericContainerWithVersion;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.IndexedRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceUnitTest {

    private static final Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Mock
    private AppConfiguration appConfiguration;

    @Mock
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    private IndexedRecord createAvroRecord() {
        String userSchema = "{\"namespace\": \"example.avro\", \"type\": \"record\", " +
                "\"name\": \"User\"," +
                "\"fields\": [{\"name\": \"name\", \"type\": \"string\"}]}";
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(userSchema);
        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("name", "testUser");
        return avroRecord;
    }

    @Test
    void whenReceiveListOfMessages_thenLogEventsAndAcknowledge() {



        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        List<Message<GenericContainerWithVersion>> listaEventos = new LinkedList<>();

        GenericContainer genericContainer = createAvroRecord();

        GenericContainerWithVersion genericContainerWithVersion = new GenericContainerWithVersion(genericContainer, 1);

        Map<String, Object> headersMap = new HashMap<>();

        headersMap.put("specversion", "1".getBytes());

        KafkaMessageHeaders kafkaMessageHeaders = new KafkaMessageHeaders(headersMap, false, false);

        kafkaMessageHeaders.getRawHeaders().put("id", "03b6682c-573b-a724-2470-0ef1876ca188".getBytes());

        GenericMessage<GenericContainerWithVersion> message = new GenericMessage<>(genericContainerWithVersion, kafkaMessageHeaders);

        listaEventos.add(message);

        Acknowledgment ack = mock(Acknowledgment.class);

        when(this.appConfiguration.getCommit()).thenReturn(true);

        assertDoesNotThrow(() -> this.kafkaConsumerService.consume(listaEventos, ack));

        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Headers: {\"specversion\":\"1\",\"id\":\"03b6682c-573b-a724-2470-0ef1876ca188\"} | Payload: {\"name\": \"testUser\"}"));

        verify(ack).acknowledge();

        log.detachAppender(listAppender);

    }

    @Test
    void whenStartAndContainerIsNotNull_thenCallStartMethod() {

        MessageListenerContainer msgListenerContainer = mock(MessageListenerContainer.class);

        when(this.kafkaListenerEndpointRegistry.getListenerContainer(any())).thenReturn(msgListenerContainer);

        this.kafkaConsumerService.start();

        verify(msgListenerContainer).start();

    }

    @Test
    void whenStartAndContainerIsNull_thenCallStartMethod() {

        when(this.kafkaListenerEndpointRegistry.getListenerContainer(any())).thenReturn(null);

        assertDoesNotThrow(() -> this.kafkaConsumerService.start());

    }

    public static class KafkaMessageHeaders extends MessageHeaders {

        /**
         * Construct headers with or without id and/or timestamp.
         * @param generateId true to add an ID header.
         * @param generateTimestamp true to add a timestamp header.
         */
        KafkaMessageHeaders(Map<String, Object> map, boolean generateId, boolean generateTimestamp) {
            super(map, generateId ? null : ID_VALUE_NONE, generateTimestamp ? null : -1L);
        }

        public Map<String, Object> getRawHeaders() { //NOSONAR - not useless, widening to public
            return super.getRawHeaders();
        }

    }

}
