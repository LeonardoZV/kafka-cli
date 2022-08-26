package br.com.leonardozv.kafka.cli.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppConfigurationUnitTest {

    @Test
    void teste() {

        String[] topics = new String[] { "test" };

        AppConfiguration appConfiguration = AppConfiguration.builder()
                .applicationId("test")
                .schemaFolderLocation("test")
                .keyFolderLocation("test")
                .headerFolderLocation("test")
                .payloadFolderLocation("test")
                .action("test")
                .topics(topics)
                .groupId("test")
                .commit(false)
                .topic("test")
                .schema("test")
                .header(false)
                .key(false)
                .batches(1)
                .events(1L)
                .build();

        assertEquals("test", appConfiguration.getApplicationId());
        assertEquals("test", appConfiguration.getSchemaFolderLocation());
        assertEquals("test", appConfiguration.getKeyFolderLocation());
        assertEquals("test", appConfiguration.getHeaderFolderLocation());
        assertEquals("test", appConfiguration.getPayloadFolderLocation());
        assertEquals("test", appConfiguration.getAction());
        assertEquals(topics, appConfiguration.getTopics());
        assertEquals("test", appConfiguration.getGroupId());
        assertEquals(false, appConfiguration.getCommit());
        assertEquals("test", appConfiguration.getTopic());
        assertEquals("test", appConfiguration.getSchema());
        assertEquals(false, appConfiguration.getHeader());
        assertEquals(false, appConfiguration.getKey());
        assertEquals(1, appConfiguration.getBatches());
        assertEquals(1L, appConfiguration.getEvents());

    }
}
