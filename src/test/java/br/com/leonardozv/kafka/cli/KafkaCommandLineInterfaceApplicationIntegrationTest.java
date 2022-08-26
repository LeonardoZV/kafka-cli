package br.com.leonardozv.kafka.cli;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class KafkaCommandLineInterfaceApplicationIntegrationTest {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> KafkaCommandLineInterfaceApplication.main(new String[]{}));
    }

}
