package br.com.leonardozv.kafka.cli;

import br.com.leonardozv.kafka.cli.services.CommandLineInterfaceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaCommandLineInterfaceRunner implements CommandLineRunner {

    private final CommandLineInterfaceService commandLineInterfaceService;

    public KafkaCommandLineInterfaceRunner(CommandLineInterfaceService commandLineInterfaceService) {
        this.commandLineInterfaceService = commandLineInterfaceService;
    }

    @Override
    public void run(String... args) throws IOException, InterruptedException {
        this.commandLineInterfaceService.execute();
    }

}
