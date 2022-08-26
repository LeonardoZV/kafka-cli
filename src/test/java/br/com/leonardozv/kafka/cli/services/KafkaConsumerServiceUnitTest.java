package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceUnitTest {

    private static final String APPLICATION_ID = "kafka-cli";

    @Mock
    private AppConfiguration appConfiguration;

    @Mock
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

//    @Test
//    void whenReceiveListOfMessages_thenLogEventsAndAcknowledge() {
//
//        List<Message<GenericContainerWithVersion>> listaEventos = new LinkedList<>();
//
//        GenericContainer genericContainer = () -> null;
//
//        GenericContainerWithVersion genericContainerWithVersion = new GenericContainerWithVersion(genericContainer, 1);
//
//        Acknowledgment ack = mock(Acknowledgment.class);
//
//        when(this.appConfiguration.getCommit()).thenReturn(true);
//
//        assertDoesNotThrow(() -> this.kafkaConsumerService.consume(null, ack));
//
//        verify(ack, times(1)).acknowledge();
//
//    }

    @Test
    void whenStartAndContainerIsNotNull_thenCallStartMethod() {

        MessageListenerContainer msgListenerContainer = mock(MessageListenerContainer.class);

        when(this.kafkaListenerEndpointRegistry.getListenerContainer(APPLICATION_ID)).thenReturn(msgListenerContainer);

        this.kafkaConsumerService.start();

        verify(msgListenerContainer, times(1)).start();

    }

    @Test
    void whenStartAndContainerIsNull_thenCallStartMethod() {

        when(this.kafkaListenerEndpointRegistry.getListenerContainer(APPLICATION_ID)).thenReturn(null);

        assertDoesNotThrow(() -> this.kafkaConsumerService.start());

    }



}
