package br.com.itau.kafka.cli.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"topic", "partition", "offset", "specversion", "type", "source" ,"id", "time", "messageversion", "eventversion", "transactionid" ,"correlationid", "datacontenttype" })
public class CloudEventsMessageHeader {

	@JsonInclude(Include.NON_NULL)
	private String topic;
	
	@JsonInclude(Include.NON_NULL)
	private int partition;
	
	@JsonInclude(Include.NON_NULL)
	private long offset;
	
	@JsonInclude(Include.NON_NULL)
	private String specversion;
	
	@JsonInclude(Include.NON_NULL)
	private String type;
	
	@JsonInclude(Include.NON_NULL)
	private String source;
	
	@JsonInclude(Include.NON_NULL)
	private String id;
	
	@JsonInclude(Include.NON_NULL)
	private String time;
	
	@JsonInclude(Include.NON_NULL)
	private String messageversion;
	
	@JsonInclude(Include.NON_NULL)
	private String eventversion;
	
	@JsonInclude(Include.NON_NULL)
	private String transactionid;
	
	@JsonInclude(Include.NON_NULL)
	private String correlationid;
		
	@JsonInclude(Include.NON_NULL)
	private String datacontenttype;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getSpecversion() {
		return specversion;
	}

	public void setSpecversion(String specversion) {
		this.specversion = specversion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMessageversion() {
		return messageversion;
	}

	public void setMessageversion(String messageversion) {
		this.messageversion = messageversion;
	}

	public String getEventversion() {
		return eventversion;
	}

	public void setEventversion(String eventversion) {
		this.eventversion = eventversion;
	}

	public String getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public String getCorrelationid() {
		return correlationid;
	}

	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}

	public String getDatacontenttype() {
		return datacontenttype;
	}

	public void setDatacontenttype(String datacontenttype) {
		this.datacontenttype = datacontenttype;
	}	
	
}
