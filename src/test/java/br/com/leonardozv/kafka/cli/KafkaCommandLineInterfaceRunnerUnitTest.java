package br.com.leonardozv.kafka.cli;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import br.com.leonardozv.kafka.cli.services.FakeDataProducerService;
import br.com.leonardozv.kafka.cli.services.KafkaConsumerService;
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
import org.springframework.context.support.GenericApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaCommandLineInterfaceRunnerUnitTest {

    private static final Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Mock
    private GenericApplicationContext appContext;

    @Mock
    private AppConfiguration appConfiguration;

    @Mock
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private FakeDataProducerService fakeDataProducerService;

    @InjectMocks
    private KafkaCommandLineInterfaceRunner kafkaCommandLineInterfaceRunner;

    @Test
    void whenActionIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getTopics()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

        assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Parâmetro 'topics' não informado."));

        log.detachAppender(listAppender);

    }

    @Test
    void whenActionIsConsumeAndEverythingIsOK_thenCallStartMethod() throws Exception {

        when(this.appConfiguration.getAction()).thenReturn("consume");
        when(this.appConfiguration.getTopics()).thenReturn(new String[]{ "accounting-journal-entry-created" });

        this.kafkaCommandLineInterfaceRunner.run();

        verify(this.appContext).registerBean(KafkaConsumerService.class);

        verify(this.appContext).getBean(KafkaConsumerService.class);

    }

    @Test
    void whenActionIsProduceAndTopicIsNull_thenLogError() throws Exception {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

        listAppender.start();

        log.addAppender(listAppender);

        when(this.appConfiguration.getAction()).thenReturn("produce");
        when(this.appConfiguration.getTopic()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchema()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchemaFolderLocation()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getKey()).thenReturn(true);
        when(this.appConfiguration.getKeyFolderLocation()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getHeader()).thenReturn(true);
        when(this.appConfiguration.getHeaderFolderLocation()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getPayloadFolderLocation()).thenReturn(null);

        this.kafkaCommandLineInterfaceRunner.run();

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
        when(this.appConfiguration.getSchemaFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\schemas\\");
        when(this.appConfiguration.getKey()).thenReturn(true);
        when(this.appConfiguration.getKeyFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\keys\\");
        when(this.appConfiguration.getHeader()).thenReturn(true);
        when(this.appConfiguration.getHeaderFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\headers\\");
        when(this.appConfiguration.getPayloadFolderLocation()).thenReturn("D:\\source\\kafka-cli-java\\src\\main\\resources\\payloads\\");
        when(this.appConfiguration.getBatches()).thenReturn(1);
        when(this.appConfiguration.getEvents()).thenReturn(1L);

        this.kafkaCommandLineInterfaceRunner.run();

        verify(this.fakeDataProducerService).generateAndProduceEvents(any(), any(), any(), any(), any());

        assertEquals(Level.INFO, listAppender.list.get(0).getLevel());
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains("Foram postados 1 evento(s)"));

        log.detachAppender(listAppender);

    }

}
