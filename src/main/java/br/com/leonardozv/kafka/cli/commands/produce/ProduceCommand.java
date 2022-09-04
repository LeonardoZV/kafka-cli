package br.com.leonardozv.kafka.cli.commands.produce;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import br.com.leonardozv.kafka.cli.services.FakeDataProducerService;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "produce")
public class ProduceCommand implements Callable<Integer> {

    private static final Logger log = LoggerFactory.getLogger(ProduceCommand.class);

    private final AppConfiguration appConfiguration;

    private final FakeDataProducerService fakeDataProducerService;

    @Option(names = {"-t", "--topic"}, required = true)
    private String topic;

    @Option(names = {"-s", "--schema"}, required = true)
    private String schemaName;

    @Option(names = {"-b", "--batches"}, defaultValue = "1")
    private Integer batches;

    @Option(names = {"-e", "--events"}, defaultValue = "1")
    private Long eventsPerBatch;

    @Option(names = {"-k", "--with-key"}, defaultValue = "false")
    private Boolean withKey;

    @Option(names = {"-h", "--with-header"}, defaultValue = "false")
    private Boolean withHeader;

    public ProduceCommand(AppConfiguration appConfiguration, FakeDataProducerService fakeDataProducerService) {
        this.appConfiguration = appConfiguration;
        this.fakeDataProducerService = fakeDataProducerService;
    }

    @Override
    public Integer call() throws IOException {

        String header;

        if (Boolean.TRUE.equals(withHeader)) {

            if (this.appConfiguration.getHeaderLocation() == null) {
                log.error("Configuração 'header.location' não encontrada.");
                return 0;
            }

            header = Files.readString(Paths.get(this.appConfiguration.getHeaderLocation() + schemaName + ".json"));

        } else {
            header = null;
        }

        String key;
        Schema keySchema;

        if (Boolean.TRUE.equals(withKey)) {

            if (this.appConfiguration.getKeyLocation() == null) {
                log.error("Configuração 'key.location' não encontrada.");
                return 0;
            }

            key = Files.readString(Paths.get(this.appConfiguration.getKeyLocation() + schemaName + ".json"));
            keySchema = new Schema.Parser().parse(Files.readString(Paths.get(this.appConfiguration.getKeySchemaLocation() + schemaName + ".avsc")));

        } else {
            key = null;
            keySchema = null;
        }

        if (this.appConfiguration.getValueLocation() == null) {
            log.error("Configuração 'value.location' não encontrada.");
            return 0;
        }

        String value = Files.readString(Paths.get(this.appConfiguration.getValueLocation() + schemaName + ".json"));
        Schema valueSchema = new Schema.Parser().parse(Files.readString(Paths.get(this.appConfiguration.getValueSchemaLocation() + schemaName + ".avsc")));

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        this.fakeDataProducerService.generateAndProduceEvents(topic, header, key, keySchema, value, valueSchema, batches, eventsPerBatch);

        stopWatch.stop();

        long amountEventsProduced = getAmountEventsProduced(batches, eventsPerBatch);

        double amountEventsProducedPerSecond = getAmountEventsProducedPerSecond(amountEventsProduced, stopWatch.getTotalTimeSeconds());

        log.info("{} message(s) | {} second(s) | {} messages/s.", amountEventsProduced, stopWatch.getTotalTimeSeconds(), amountEventsProducedPerSecond);

        return 0;

    }

    private long getAmountEventsProduced(Integer batches, Long events) {
        return batches * events;
    }

    private double getAmountEventsProducedPerSecond(long amountEventsProduced, double totalTimeSeconds) {
        return amountEventsProduced / totalTimeSeconds;
    }

}
