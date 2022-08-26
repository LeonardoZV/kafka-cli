package br.com.leonardozv.kafka.cli.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompleteMessageHeaderUnitTest {

    @Test
    void whenConstructed_thenReturnExactlyAsConstructed() {

        CompleteMessageHeader header = CompleteMessageHeader.builder()
                .topic("1")
                .partition(1)
                .offset(1)
                .specversion("1")
                .type("1")
                .source("1")
                .id("1")
                .time("1")
                .messageversion("1")
                .eventversion("1")
                .transactionid("1")
                .correlationid("1")
                .datacontenttype("1")
                .build();

        assertEquals("1", header.getTopic());
        assertEquals(1, header.getPartition());
        assertEquals(1, header.getOffset());
        assertEquals("1", header.getSpecversion());
        assertEquals("1", header.getType());
        assertEquals("1", header.getSource());
        assertEquals("1", header.getId());
        assertEquals("1", header.getTime());
        assertEquals("1", header.getMessageversion());
        assertEquals("1", header.getEventversion());
        assertEquals("1", header.getTransactionid());
        assertEquals("1", header.getCorrelationid());
        assertEquals("1", header.getCorrelationid());

    }

}
