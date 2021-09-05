package br.com.leonardozv.kafka.cli.mappers;

import br.com.leonardozv.kafka.cli.models.CloudEventsMessageHeader;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.StandardCharsets;

public final class CloudEventsMessageHeaderMapper {

	public static CloudEventsMessageHeader from(MessageHeaders headers) {
				
		CloudEventsMessageHeader header = new CloudEventsMessageHeader();		

		Object topic = headers.get(KafkaHeaders.RECEIVED_TOPIC);

		if (topic != null)
			header.setTopic(topic.toString());

		Object partition = headers.get(KafkaHeaders.RECEIVED_PARTITION_ID);

		if (partition != null)
			header.setPartition(Integer.parseInt(partition.toString()));

		Object offset = headers.get(KafkaHeaders.OFFSET);

		if (offset!= null)
			header.setOffset(Long.parseLong(offset.toString()));

		Object specversion = headers.get("specversion");

		if (specversion != null)
			header.setSpecversion(new String(((byte[]) specversion), StandardCharsets.UTF_8));

		Object type = headers.get("type");

		if (type != null)
			header.setType(new String(((byte[]) type), StandardCharsets.UTF_8));

		Object source = headers.get("source");

		if (source != null)
			header.setSource(new String(((byte[]) source), StandardCharsets.UTF_8));

		Object id = headers.get("id");

		if (id != null)
			header.setId(new String(((byte[]) id), StandardCharsets.UTF_8));

		Object time = headers.get("time");

		if (time != null)
			header.setTime(new String(((byte[]) time), StandardCharsets.UTF_8));

		Object messageversion = headers.get("messageversion");

		if (messageversion != null)
			header.setMessageversion(new String(((byte[]) messageversion), StandardCharsets.UTF_8));

		Object eventversion = headers.get("eventversion");

		if (eventversion != null)
			header.setEventversion(new String(((byte[]) eventversion), StandardCharsets.UTF_8));

		Object transactionid = headers.get("transactionid");

		if (transactionid != null)
			header.setTransactionid(new String(((byte[]) transactionid), StandardCharsets.UTF_8));

		Object correlationid = headers.get("correlationid");

		if (correlationid != null)
			header.setCorrelationid(new String(((byte[]) correlationid), StandardCharsets.UTF_8));

		Object datacontenttype = headers.get("datacontenttype");

		if (datacontenttype != null)
			header.setDatacontenttype(new String(((byte[]) datacontenttype), StandardCharsets.UTF_8));
		
		return header;
		
	}
	
}
