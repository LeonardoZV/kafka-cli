package br.com.leonardozv.kafka.cli.models;

import br.com.leonardozv.kafka.cli.serializers.ByteArrayToStringDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonPropertyOrder({"specversion", "type", "source" ,"id", "time", "messageversion", "eventversion", "transactionid" ,"correlationid", "datacontenttype" })
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CloudEventsMessageHeader {

	@JsonProperty("specversion")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String specversion;

	@JsonProperty("type")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String type;

	@JsonProperty("source")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String source;

	@JsonProperty("id")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String id;

	@JsonProperty("time")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String time;

	@JsonProperty("messageversion")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String messageversion;

	@JsonProperty("eventversion")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String eventversion;

	@JsonProperty("transactionid")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String transactionid;

	@JsonProperty("correlationid")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String correlationid;

	@JsonProperty("datacontenttype")
	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String datacontenttype;

}
