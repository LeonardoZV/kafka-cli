package br.com.leonardozv.kafka.cli;

import br.com.leonardozv.kafka.cli.commands.RootCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class KafkaCommandLineInterfaceRunner implements CommandLineRunner, ExitCodeGenerator {

    private final RootCommand rootCommand;

    private final IFactory factory;

    private int exitCode;

    public KafkaCommandLineInterfaceRunner(RootCommand rootCommand, IFactory factory) {
        this.rootCommand = rootCommand;
        this.factory = factory;
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(rootCommand, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}