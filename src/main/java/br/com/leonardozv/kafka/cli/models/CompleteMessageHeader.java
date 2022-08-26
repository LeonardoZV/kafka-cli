package br.com.leonardozv.kafka.cli.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.kafka.support.KafkaHeaders;

@JsonPropertyOrder({"topic", "partition", "offset", "specversion", "type", "source" ,"id", "time", "messageversion", "eventversion", "transactionid" ,"correlationid", "datacontenttype" })
@SuperBuilder
@Getter
@Setter
public class CompleteMessageHeader extends CloudEventsMessageHeader {

    @JsonAlias({ KafkaHeaders.RECEIVED_TOPIC })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String topic;

    @JsonAlias({ KafkaHeaders.RECEIVED_PARTITION_ID })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int partition;

    @JsonAlias({ KafkaHeaders.OFFSET })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long offset;

}
