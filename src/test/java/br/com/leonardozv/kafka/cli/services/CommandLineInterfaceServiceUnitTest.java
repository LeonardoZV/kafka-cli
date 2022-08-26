package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'action' não informado."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionDifferentThanConsumeOrProduce_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("banana");

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'action' inválido."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsConsumeAndTopicsIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("consume");
        when(this.appConfiguration.getTopics()).thenReturn(new String[]{ "default" });

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'topics' não informado."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsConsumeAndEverythingIsOK_thenCallStartMethod() throws Exception {

        when(this.appConfiguration.getAction()).thenReturn("consume");
        when(this.appConfiguration.getTopics()).thenReturn(new String[]{ "accounting-journal-entry-created" });

        this.commandLineInterfaceService.execute();

        verify(this.kafkaConsumerService).start();

    }

    @Test
    void whenActionIsProduceAndTopicIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("default");

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'topic' não informado."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndSchemaIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("default");

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'schema' não informado."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndApplicationSchemaFolderLocationIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Configuração 'application.schema.folder.location' não encontrada."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndKeyIsTrueAndApplicationKeyFolderLocationIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getKey()).thenReturn(true);
        when(this.appConfiguration.getApplicationKeyFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Configuração 'application.key.folder.location' não encontrada."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndHeaderIsTrueAndApplicationHeaderFolderLocationIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getHeader()).thenReturn(true);
        when(this.appConfiguration.getApplicationHeaderFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Configuração 'application.header.folder.location' não encontrada."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndApplicationPayloadFolderLocationIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn("accounting-journal-entry-created");
        when(this.appConfiguration.getSchema()).thenReturn("br.com.leonardozv.exemplos.accounting_journal_entry_created");
        when(this.appConfiguration.getApplicationSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getApplicationPayloadFolderLocation()).thenReturn(null);

        this.commandLineInterfaceService.execute();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Configuração 'application.payload.folder.location' não encontrada."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsProduceAndEverythingIsOK_thenCallGenerateAndProduceMethodAndLogMetrics() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

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

        verify(this.fakeDataProducerService).generateAndProduceEvents(any(), any(), any(), any(), any());

        assertEquals(Level.INFO, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Foram postados 1 evento(s)"));

        log.detachAppender(listAppender);

    }

}
