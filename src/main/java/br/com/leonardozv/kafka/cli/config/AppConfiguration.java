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

	private String schemaFolderLocation  = null;
	private String keyFolderLocation = null;
	private String headerFolderLocation = null;
	private String payloadFolderLocation  = null;

}
