package br.com.leonardozv.kafka.cli;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import br.com.leonardozv.kafka.cli.services.FakeDataProducerService;
import br.com.leonardozv.kafka.cli.services.KafkaConsumerService;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class KafkaCommandLineInterfaceRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KafkaCommandLineInterfaceRunner.class);
    private final GenericApplicationContext appContext;

    private final AppConfiguration appConfiguration;

    private final FakeDataProducerService fakeDataProducerService;

    public KafkaCommandLineInterfaceRunner(GenericApplicationContext appContext, AppConfiguration appConfiguration, FakeDataProducerService fakeDataProducerService) {
        this.appContext = appContext;
        this.appConfiguration = appConfiguration;
        this.fakeDataProducerService = fakeDataProducerService;
    }

    @Override
    public void run(String... args) throws IOException, InterruptedException {

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

        if (this.appConfiguration.getTopics() == null) {
            log.error("Parâmetro 'topics' não informado.");
        }

         this.appContext.registerBean(KafkaConsumerService.class);

         this.appContext.getBean(KafkaConsumerService.class);

    }

    private void produceAction() throws IOException, InterruptedException {

        if (this.appConfiguration.getTopic() == null) {
            log.error("Parâmetro 'topic' não informado.");
            return;
        }

        if (this.appConfiguration.getSchema() == null) {
            log.error("Parâmetro 'schema' não informado.");
            return;
        }

        if (this.appConfiguration.getSchemaFolderLocation() == null) {
            log.error("Configuração 'application.schema.folder.location' não encontrada.");
            return;
        }

        Schema schema = new Schema.Parser().parse(Files.readString(Paths.get(this.appConfiguration.getSchemaFolderLocation() + this.appConfiguration.getSchema() + ".avsc")));

        String key = null;

        if (Boolean.TRUE.equals(this.appConfiguration.getKey())) {

            if (this.appConfiguration.getKeyFolderLocation() == null) {
                log.error("Configuração 'application.key.folder.location' não encontrada.");
                return;
            }

            key = Files.readString(Paths.get(this.appConfiguration.getKeyFolderLocation() + this.appConfiguration.getSchema() + ".json"));

        }

        String header = null;

        if (Boolean.TRUE.equals(this.appConfiguration.getHeader())) {

            if (this.appConfiguration.getHeaderFolderLocation() == null) {
                log.error("Configuração 'application.header.folder.location' não encontrada.");
                return;
            }

            header = Files.readString(Paths.get(this.appConfiguration.getHeaderFolderLocation() + this.appConfiguration.getSchema() + ".json"));

        }

        if (this.appConfiguration.getPayloadFolderLocation() == null) {
            log.error("Configuração 'application.payload.folder.location' não encontrada.");
            return;
        }

        String payload = Files.readString(Paths.get(this.appConfiguration.getPayloadFolderLocation() + this.appConfiguration.getSchema() + ".json"));

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
