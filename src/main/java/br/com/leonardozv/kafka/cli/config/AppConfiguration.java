package br.com.leonardozv.kafka.cli.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

	@Value("${application.header.folder.location}")
	private String applicationHeaderFolderLocation;

	@Value("${application.key.folder.location}")
	private String applicationKeyFolderLocation;
	
	@Value("${application.payload.folder.location}")
	private String applicationPayloadFolderLocation;

	@Value("${application.schema.folder.location}")
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

	public String getApplicationHeaderFolderLocation() {
		return applicationHeaderFolderLocation;
	}

	public void setApplicationHeaderFolderLocation(String applicationHeaderFolderLocation) {
		this.applicationHeaderFolderLocation = applicationHeaderFolderLocation;
	}

	public String getApplicationKeyFolderLocation() {
		return applicationKeyFolderLocation;
	}

	public void setApplicationKeyFolderLocation(String applicationKeyFolderLocation) {
		this.applicationKeyFolderLocation = applicationKeyFolderLocation;
	}

	public String getApplicationPayloadFolderLocation() {
		return applicationPayloadFolderLocation;
	}

	public void setApplicationPayloadFolderLocation(String applicationPayloadFolderLocation) {
		this.applicationPayloadFolderLocation = applicationPayloadFolderLocation;
	}

	public String getApplicationSchemaFolderLocation() {
		return applicationSchemaFolderLocation;
	}

	public void setApplicationSchemaFolderLocation(String applicationSchemaFolderLocation) {
		this.applicationSchemaFolderLocation = applicationSchemaFolderLocation;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Boolean getCommit() {
		return commit;
	}

	public void setCommit(Boolean commit) {
		this.commit = commit;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Boolean getHeader() {
		return header;
	}

	public void setHeader(Boolean header) {
		this.header = header;
	}

	public Boolean getKey() {
		return key;
	}

	public void setKey(Boolean key) {
		this.key = key;
	}

	public Integer getBatches() {
		return batches;
	}

	public void setBatches(Integer batches) {
		this.batches = batches;
	}

	public Long getEvents() {
		return events;
	}

	public void setEvents(Long events) {
		this.events = events;
	}
	
}
