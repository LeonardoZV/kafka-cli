package br.com.itau.kafka.cli.mappers;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;

import br.com.itau.kafka.cli.models.CloudEventsMessageHeader;

public final class CloudEventsMessageHeaderMapper {

	public static CloudEventsMessageHeader from(MessageHeaders headers) throws Exception {
				
		CloudEventsMessageHeader header = new CloudEventsMessageHeader();		
		
		if (headers.get(KafkaHeaders.RECEIVED_TOPIC) != null)
			header.setTopic(headers.get(KafkaHeaders.RECEIVED_TOPIC).toString());
		
		if (headers.get(KafkaHeaders.RECEIVED_PARTITION_ID) != null)
			header.setPartition(Integer.parseInt(headers.get(KafkaHeaders.RECEIVED_PARTITION_ID).toString()));
		
		if (headers.get(KafkaHeaders.OFFSET) != null)
			header.setOffset(Long.parseLong(headers.get(KafkaHeaders.OFFSET).toString()));
		
		if (headers.get("specversion") != null)
			header.setSpecversion(new String(((byte[]) headers.get("specversion")), "UTF-8").toString());
		
		if (headers.get("type") != null)
			header.setType(new String(((byte[]) headers.get("type")), "UTF-8").toString());
		
		if (headers.get("source") != null)
			header.setSource(new String(((byte[]) headers.get("source")), "UTF-8").toString());
		
		if (headers.get("id") != null)
			header.setId(new String(((byte[]) headers.get("id")), "UTF-8").toString());
		
		if (headers.get("time") != null)
			header.setTime(new String(((byte[]) headers.get("time")), "UTF-8").toString());
		
		if (headers.get("messageversion") != null)
			header.setMessageversion(new String(((byte[]) headers.get("messageversion")), "UTF-8").toString());
		
		if (headers.get("eventversion") != null)
			header.setEventversion(new String(((byte[]) headers.get("eventversion")), "UTF-8").toString());
				
		if (headers.get("transactionid") != null)
			header.setTransactionid(new String(((byte[]) headers.get("transactionid")), "UTF-8").toString());
		
		if (headers.get("correlationid") != null)
			header.setCorrelationid(new String(((byte[]) headers.get("correlationid")), "UTF-8").toString());
				
		if (headers.get("datacontenttype") != null)
			header.setDatacontenttype(new String(((byte[]) headers.get("datacontenttype")), "UTF-8").toString());
		
		return header;
		
	}
	
}
