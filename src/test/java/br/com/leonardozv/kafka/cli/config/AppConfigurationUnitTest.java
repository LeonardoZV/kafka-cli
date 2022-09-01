package br.com.leonardozv.kafka.cli.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppConfigurationUnitTest {

    @Test
    void teste() {

        String[] topics = new String[] { "test" };

        AppConfiguration appConfiguration = AppConfiguration.builder()
                .schemaFolderLocation("test")
                .keyFolderLocation("test")
                .headerFolderLocation("test")
                .payloadFolderLocation("test")
                .build();

        assertEquals("test", appConfiguration.getSchemaFolderLocation());
        assertEquals("test", appConfiguration.getKeyFolderLocation());
        assertEquals("test", appConfiguration.getHeaderFolderLocation());
        assertEquals("test", appConfiguration.getPayloadFolderLocation());

    }
}
