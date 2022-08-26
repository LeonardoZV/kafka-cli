package br.com.leonardozv.kafka.cli.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ByteArrayToStringDeserializer extends StdDeserializer<String> {

    public ByteArrayToStringDeserializer() {
        this(null);
    }

    public ByteArrayToStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        return new String(jsonparser.getBinaryValue(), StandardCharsets.UTF_8);
    }

}