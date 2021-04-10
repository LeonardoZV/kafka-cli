package br.com.leonardozv.kafka.cli.mappers;

import br.com.leonardozv.kafka.cli.models.CloudEventsMessageHeader;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;

public final class CloudEventsMessageHeaderMapper {

	public static CloudEventsMessageHeader from(MessageHeaders headers) {
				
		CloudEventsMessageHeader header = new CloudEventsMessageHeader();		
		
		if (headers.get(KafkaHeaders.RECEIVED_TOPIC) != null)
			header.setTopic(headers.get(KafkaHeaders.RECEIVED_TOPIC).toString());
		
		if (headers.get(KafkaHeaders.RECEIVED_PARTITION_ID) != null)
			header.setPartition(Integer.parseInt(headers.get(KafkaHeaders.RECEIVED_PARTITION_ID).toString()));
		
		if (headers.get(KafkaHeaders.OFFSET) != null)
			header.setOffset(Long.parseLong(headers.get(KafkaHeaders.OFFSET).toString()));
		
		if (headers.get("specversion") != null)
			header.setSpecversion(new String(((byte[]) headers.get("specversion")), StandardCharsets.UTF_8));
		
		if (headers.get("type") != null)
			header.setType(new String(((byte[]) headers.get("type")), StandardCharsets.UTF_8));
		
		if (headers.get("source") != null)
			header.setSource(new String(((byte[]) headers.get("source")), StandardCharsets.UTF_8));
		
		if (headers.get("id") != null)
			header.setId(new String(((byte[]) headers.get("id")), StandardCharsets.UTF_8));
		
		if (headers.get("time") != null)
			header.setTime(new String(((byte[]) headers.get("time")), StandardCharsets.UTF_8));
		
		if (headers.get("messageversion") != null)
			header.setMessageversion(new String(((byte[]) headers.get("messageversion")), StandardCharsets.UTF_8));
		
		if (headers.get("eventversion") != null)
			header.setEventversion(new String(((byte[]) headers.get("eventversion")), StandardCharsets.UTF_8));
				
		if (headers.get("transactionid") != null)
			header.setTransactionid(new String(((byte[]) headers.get("transactionid")), StandardCharsets.UTF_8));
		
		if (headers.get("correlationid") != null)
			header.setCorrelationid(new String(((byte[]) headers.get("correlationid")), StandardCharsets.UTF_8));
				
		if (headers.get("datacontenttype") != null)
			header.setDatacontenttype(new String(((byte[]) headers.get("datacontenttype")), StandardCharsets.UTF_8));
		
		return header;
		
	}
	
}
