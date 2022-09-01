package br.com.leonardozv.kafka.cli.services;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FakeDataProducerServiceUnitTest {

    @Mock
    private GenericKafkaProducerService genericKafkaProducerService;

    @InjectMocks
    private FakeDataProducerService fakeDataProducerService;

    @Test
    void whenGenerateAndProduceEvents_then() throws IOException {

        String topic = "accounting-journal-entry-created";

        Schema schema = new Schema.Parser().parse("{\"namespace\": \"example.avro\", \"type\": \"record\", \"name\": \"User\", \"fields\": [{\"name\": \"name\", \"type\": \"string\"}]}");

        String key = "ABC";

        String header = "{\"id\": \"{UUID}\" }";

        String payload = "{\"name\": \"testUser\" }";

        Integer batches = 2;

        Long eventsPerBatch = 2L;

        this.fakeDataProducerService.generateAndProduceEvents(topic, schema, key, header, payload, batches, eventsPerBatch);

        verify(this.genericKafkaProducerService, times(4)).produce(any(), any(), any(), any(), any());

        verify(this.genericKafkaProducerService, times(2)).flush();

    }

}
