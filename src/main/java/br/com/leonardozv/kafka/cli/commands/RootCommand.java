package br.com.leonardozv.kafka.cli.commands;

import br.com.leonardozv.kafka.cli.commands.admin.AdminCommand;
import br.com.leonardozv.kafka.cli.commands.consume.ConsumeCommand;
import br.com.leonardozv.kafka.cli.commands.produce.ProduceCommand;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(subcommands = { ConsumeCommand.class, ProduceCommand.class, AdminCommand.class })
public class RootCommand { }