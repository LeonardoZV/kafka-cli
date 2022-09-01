package br.com.leonardozv.kafka.cli.commands.admin;

import picocli.CommandLine.Command;

@Command(name = "admin", subcommands = { AdminDescribeTopicsCommand.class })
public class AdminCommand { }
