package br.com.leonardozv.kafka.cli;

import br.com.leonardozv.kafka.cli.services.CommandLineInterfaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaCommandLineInterfaceRunnerUnitTest {

    @Mock
    private CommandLineInterfaceService commandLineInterfaceService;

    @InjectMocks
    private KafkaCommandLineInterfaceRunner kafkaCommandLineInterfaceRunner;

    @Test
    void whenExecuted_thenCallExecuteMethod() throws IOException, InterruptedException {

        doNothing().when(this.commandLineInterfaceService).execute();

        assertDoesNotThrow(() -> this.kafkaCommandLineInterfaceRunner.run());

        verify(this.commandLineInterfaceService).execute();

    }

}
