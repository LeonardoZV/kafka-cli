package br.com.leonardozv.kafka.cli.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfiguration {

	@Value("${application.header.folder.location:#{null}}")
	private String applicationHeaderFolderLocation;

	@Value("${application.key.folder.location:#{null}}")
	private String applicationKeyFolderLocation;
	
	@Value("${application.payload.folder.location:#{null}}")
	private String applicationPayloadFolderLocation;

	@Value("${application.schema.folder.location:#{null}}")
	private String applicationSchemaFolderLocation;
	
	@Value("${action:#{null}}")
	private String action;
	
	@Value("#{new String('${topics:default}')}")
	private String[] topics;
	
	@Value("#{new String('${group-id:default}')}")
	private String groupId;
	
	@Value("#{new Boolean('${commit:true}')}")
	private Boolean commit;
	
	@Value("#{new String('${topic:default}')}")
	private String topic;
	
	@Value("#{new String('${schema:default}')}")
	private String schema;

	@Value("#{new Boolean('${header:false}')}")
	private Boolean header;

	@Value("#{new Boolean('${key:false}')}")
	private Boolean key;
	
	@Value("#{new Integer('${batches:1}')}")
	private Integer batches;
	
	@Value("#{new Long('${events:1}')}")
	private Long events;

}
