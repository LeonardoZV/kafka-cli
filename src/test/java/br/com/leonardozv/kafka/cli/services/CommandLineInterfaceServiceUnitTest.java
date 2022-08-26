package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandLineInterfaceServiceUnitTest {

    private static final Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Mock
    private AppConfiguration appConfiguration;

    @Mock
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private FakeDataProducerService fakeDataProducerService;

    @InjectMocks
    private CommandLineInterfaceService commandLineInterfaceService;

    @Test
    void whenActionIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Parâmetro 'action' não informado.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionDifferentThanConsumeOrProduce_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("banana");

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Parâmetro 'action' inválido.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsConsumeAndTopicsIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("consume");
        when(this.appConfiguration.getTopics()).thenReturn(new String[]{ "default" });

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Parâmetro 'topics' não informado.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsConsumeAndEverythingIsOK_thenCallStartMethod() throws Exception {

        when(this.appConfiguration.getAction()).thenReturn("consume");
        when(this.appConfiguration.getTopics()).thenReturn(new String[]{ "accounting-journal-entry-created" });

        this.commandLineInterfaceService.execute();

        verify(this.kafkaConsumerService, times(1)).start();

    }

    @Test
    void whenActionIsProduceAndTopicIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("default");

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Parâmetro 'topic' não informado.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndSchemaIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("default");

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Parâmetro 'schema' não informado.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndApplicationSchemaFolderLocationIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Configuração 'application.schema.folder.location' não encontrada.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndKeyIsTrueAndApplicationKeyFolderLocationIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getKey()).thenReturn(true);
        when(this.appConfiguration.getApplicationKeyFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Configuração 'application.key.folder.location' não encontrada.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndHeaderIsTrueAndApplicationHeaderFolderLocationIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getHeader()).thenReturn(true);
        when(this.appConfiguration.getApplicationHeaderFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Configuração 'application.header.folder.location' não encontrada.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndApplicationPayloadFolderLocationIsNull_thenLogError() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getApplicationPayloadFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.ERROR, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertEquals("Configuração 'application.payload.folder.location' não encontrada.", loggingEventCaptor.getAllValues().get(0).getMessage());

        log.detachAppender(mockedAppender);

    }

    @Test
    void whenActionIsProduceAndEverythingIsOK_thenCallGenerateAndProduceMethodAndLogMetrics() throws Exception {

        Appender<ILoggingEvent> mockedAppender = mock(Appender.class);

        ArgumentCaptor<LoggingEvent> loggingEventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);

        log.addAppender(mockedAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getKey()).thenReturn(true);
        when(this.appConfiguration.getApplicationKeyFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\keys\\");
        when(this.appConfiguration.getHeader()).thenReturn(true);
        when(this.appConfiguration.getApplicationHeaderFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\headers\\");
        when(this.appConfiguration.getApplicationPayloadFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\payloads\\");
        when(this.appConfiguration.getBatches()).thenReturn(1);
        when(this.appConfiguration.getEvents()).thenReturn((long) 1);

        this.commandLineInterfaceService.execute();

        verify(this.fakeDataProducerService, times(1)).generateAndProduceEvents(any(), any(), any(), any(), any());
        verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());

        assertEquals(Level.INFO, loggingEventCaptor.getAllValues().get(0).getLevel());
        assertThat(loggingEventCaptor.getAllValues().get(0).getMessage()).contains("Foram postados");

        log.detachAppender(mockedAppender);

    }

}
