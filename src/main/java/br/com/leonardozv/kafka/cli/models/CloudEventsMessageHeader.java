package br.com.leonardozv.kafka.cli.models;

import br.com.leonardozv.kafka.cli.serializers.ByteArrayToStringDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@JsonPropertyOrder({"specversion", "type", "source" ,"id", "time", "messageversion", "eventversion", "transactionid" ,"correlationid", "datacontenttype" })
@SuperBuilder
@Getter
@Setter
public class CloudEventsMessageHeader {

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String specversion;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String type;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String source;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String id;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String time;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String messageversion;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String eventversion;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String transactionid;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String correlationid;

	@JsonDeserialize(using = ByteArrayToStringDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	private String datacontenttype;
	
}
