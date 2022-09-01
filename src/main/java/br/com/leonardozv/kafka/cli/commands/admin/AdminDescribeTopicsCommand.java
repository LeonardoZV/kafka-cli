package br.com.leonardozv.kafka.cli.commands.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "describe-topics")
public class AdminDescribeTopicsCommand implements Callable<Integer> {

    private static final Logger log = LoggerFactory.getLogger(AdminDescribeTopicsCommand.class);

    private KafkaAdmin admin;

    @Option(names = {"-t", "--topics"}, required = true)
    private String topics;

    public AdminDescribeTopicsCommand(KafkaAdmin admin) {
        this.admin = admin;
    }

    @Override
    public Integer call()  {

        this.admin.describeTopics(topics).forEach((topic, topicDescription) -> {
            if (log.isInfoEnabled()) {
                log.info("Topic: {} | Description: {}", topic, topicDescription.toString());
            }
        });

        return 0;

    }

}
