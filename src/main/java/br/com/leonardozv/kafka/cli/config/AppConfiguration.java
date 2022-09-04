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

	private String headerLocation = null;
	private String keyLocation = null;
	private String keySchemaLocation  = null;
	private String valueLocation  = null;
	private String valueSchemaLocation  = null;

}
