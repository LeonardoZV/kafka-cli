package br.com.leonardozv.kafka.cli.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("topic")
    @JsonAlias({ KafkaHeaders.RECEIVED_TOPIC })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String topic;

    @JsonProperty("partition")
    @JsonAlias({ KafkaHeaders.RECEIVED_PARTITION_ID })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer partition;

    @JsonProperty("offset")
    @JsonAlias({ KafkaHeaders.OFFSET })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long offset;

    public CompleteMessageHeader() {
        super();
    }

    public CompleteMessageHeader(String specversion, String type, String source, String id, String time, String messageversion, String eventversion, String transactionid, String correlationid, String datacontenttype, String topic, Integer partition, Long offset) {
        super(specversion, type, source, id, time, messageversion, eventversion, transactionid, correlationid, datacontenttype);
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }

}
