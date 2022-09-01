package br.com.leonardozv.kafka.cli.commands.consume;

import br.com.leonardozv.kafka.cli.services.GenericKafkaConsumerService;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "consume")
public class ConsumeCommand implements Callable<Integer> {

    private final KafkaListenerContainerFactory kafkaListenerContainerFactory;

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Option(names = {"-t", "--topics"}, required = true)
    private String topics;

    @Option(names = {"-c", "--commit"}, arity = "0..1", defaultValue = "true", fallbackValue = "true")
    private Boolean commit;

    @Option(names = {"-g", "--group-id"}, required = true)
    private String groupId;

    public ConsumeCommand(KafkaListenerContainerFactory kafkaListenerContainerFactory, KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @Override
    public Integer call() throws NoSuchMethodException {

        MethodKafkaListenerEndpoint endpoint = new MethodKafkaListenerEndpoint();

        endpoint.setId("kafka-cli");
        endpoint.setTopics(topics);
        endpoint.setGroupId(groupId);
        endpoint.setBatchListener(true);
        endpoint.setBean(new GenericKafkaConsumerService(commit));
        endpoint.setMethod(GenericKafkaConsumerService.class.getMethod("onMessage", List.class, Acknowledgment.class));
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());

        this.kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, this.kafkaListenerContainerFactory, true);

        return 0;

    }

}
