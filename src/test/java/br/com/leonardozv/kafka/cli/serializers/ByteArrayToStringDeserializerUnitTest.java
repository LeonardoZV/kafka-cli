package br.com.leonardozv.kafka.cli.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ByteArrayToStringDeserializerUnitTest {

    @Test
    void whenReceiveBytes_thenDeserializeToString() throws IOException {

        String expectedString = "testValue";

        ByteArrayToStringDeserializer deserializer = new ByteArrayToStringDeserializer();

        JsonParser parser = mock(JsonParser.class);

        when(parser.getBinaryValue()).thenReturn(expectedString.getBytes());

        String actualString = deserializer.deserialize(parser, mock(DeserializationContext.class));

        assertEquals(expectedString, actualString);

    }

}
