package br.com.leonardozv.kafka.cli.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AppConfiguration {

	private String applicationId = "kafka-cli";
	private String schemaFolderLocation  = null;
	private String keyFolderLocation = null;
	private String headerFolderLocation = null;
	private String payloadFolderLocation  = null;
	private String action = null;
	private String[] topics = null;
	private String groupId = null;
	private Boolean commit = true;
	private String topic = null;
	private String schema = null;
	private Boolean header = false;
	private Boolean key = false;
	private Integer batches = 1;
	private Long events = 1L;

}
