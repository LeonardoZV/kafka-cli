package br.com.leonardozv.kafka.cli.services;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CommandLineInterfaceService {

    private static final Logger log = LoggerFactory.getLogger(CommandLineInterfaceService.class);

    private final AppConfiguration appConfiguration;

    private final KafkaConsumerService kafkaConsumerService;

    private final FakeDataProducerService fakeDataProducerService;

    public CommandLineInterfaceService(AppConfiguration appConfiguration, KafkaConsumerService kafkaConsumerService, FakeDataProducerService fakeDataProducerService) {
        this.appConfiguration = appConfiguration;
        this.kafkaConsumerService = kafkaConsumerService;
        this.fakeDataProducerService = fakeDataProducerService;
    }

    public void execute() throws IOException, InterruptedException {

        if (this.appConfiguration.getAction() == null) {
            log.error("Parâmetro 'action' não informado.");
            return;
        }

        switch(this.appConfiguration.getAction()) {

            case "consume":
                consumeAction();
                break;

            case "produce":
                produceAction();
                break;

            default:
                log.error("Parâmetro 'action' inválido.");
                break;

        }

    }

    private void consumeAction() {

        if (this.appConfiguration.getTopics()[0].equals("default")) {
            log.error("Parâmetro 'topics' não informado.");
        }

        this.kafkaConsumerService.start();

    }

    private void produceAction() throws IOException, InterruptedException {

        if (this.appConfiguration.getTopic().equals("default")) {
            log.error("Parâmetro 'topic' não informado.");
            return;
        }

        if (this.appConfiguration.getSchema().equals("default")) {
            log.error("Parâmetro 'schema' não informado.");
            return;
        }

        if (this.appConfiguration.getApplicationSchemaFolderLocation() == null) {
            log.error("Configuração 'application.schema.folder.location' não encontrada.");
            return;
        }

        Schema schema = new Schema.Parser().parse(Files.readString(Paths.get(this.appConfiguration.getApplicationSchemaFolderLocation() + this.appConfiguration.getSchema() + ".avsc")));

        String key = null;

        if (Boolean.TRUE.equals(this.appConfiguration.getKey())) {

            if (this.appConfiguration.getApplicationKeyFolderLocation() == null) {
                log.error("Configuração 'application.key.folder.location' não encontrada.");
                return;
            }

            key = Files.readString(Paths.get(this.appConfiguration.getApplicationKeyFolderLocation() + this.appConfiguration.getSchema() + ".json"));

        }

        String header = null;

        if (Boolean.TRUE.equals(this.appConfiguration.getHeader())) {

            if (this.appConfiguration.getApplicationHeaderFolderLocation() == null) {
                log.error("Configuração 'application.header.folder.location' não encontrada.");
                return;
            }

            header = Files.readString(Paths.get(this.appConfiguration.getApplicationHeaderFolderLocation() + this.appConfiguration.getSchema() + ".json"));

        }

        if (this.appConfiguration.getApplicationPayloadFolderLocation() == null) {
            log.error("Configuração 'application.payload.folder.location' não encontrada.");
            return;
        }

        String payload = Files.readString(Paths.get(this.appConfiguration.getApplicationPayloadFolderLocation() + this.appConfiguration.getSchema() + ".json"));

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        this.fakeDataProducerService.generateAndProduceEvents(this.appConfiguration.getTopic(), schema, key, header, payload);

        stopWatch.stop();

        long amountEventsProduced = getAmountEventsProduced(this.appConfiguration.getBatches(), this.appConfiguration.getEvents());

        double amountEventsProducedPerSecond = getAmountEventsProducedPerSecond(amountEventsProduced, stopWatch.getTotalTimeSeconds());

        log.info("Foram postados {} evento(s) em {} segundo(s), ficando {} evt/s.", amountEventsProduced, stopWatch.getTotalTimeSeconds(), amountEventsProducedPerSecond);

    }

    protected long getAmountEventsProduced(Integer batches, Long events) {
        return batches * events;
    }

    protected double getAmountEventsProducedPerSecond(long amountEventsProduced, double totalTimeSeconds) {
        return amountEventsProduced / totalTimeSeconds;
    }

}
