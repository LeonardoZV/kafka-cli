package br.com.itau.kafka.cli;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.itau.kafka.cli.config.AppConfiguration;
import br.com.itau.kafka.cli.services.KafkaConsumerService;
import br.com.itau.kafka.cli.services.KafkaProducerService;


@SpringBootApplication
public class ItauKafkaCliApplication implements CommandLineRunner {
	
	private static Logger log = LoggerFactory.getLogger(ItauKafkaCliApplication.class);
	
	@Autowired
	private AppConfiguration appConfiguration;	
	
	@Autowired
	private KafkaConsumerService kafkaConsumerService;
	
	@Autowired
	private KafkaProducerService kafkaProducerService;
	
	public static void main(String[] args) {
		
		SpringApplication.run(ItauKafkaCliApplication.class, args);
		
	}
	
    @Override
    public void run(String... args) throws Exception {
    	
    	if (this.appConfiguration.getAction() == null) {
    		log.error("Parâmetro 'action' não informado.");
    		return;
    	}
    	
    	if (!this.appConfiguration.getAction().equals("consume") && !this.appConfiguration.getAction().equals("produce")) {
    		log.error("Parâmetro 'action' inválido.");
    		return;
    	}
    	
    	if (this.appConfiguration.getAction().equals("consume")) {

    		if (this.appConfiguration.getTopics()[0].equals("default")) {
    			log.error("Parâmetro 'topics' não informado.");
    		}
    		
    		this.kafkaConsumerService.start();
    		
    	}
    	
    	if (this.appConfiguration.getAction().equals("produce")) {
    		
    		ObjectMapper mapper = new ObjectMapper();
    		
        	if (this.appConfiguration.getTopic().equals("default")) {
        		log.error("Parâmetro 'topic' não informado.");
        		return;
        	}
        	
        	String topico = this.appConfiguration.getTopic();
        	
        	if (this.appConfiguration.getSchema().equals("default")) {
        		log.error("Parâmetro 'schema' não informado.");
        		return;
        	}
        	
        	Schema schema = new Schema.Parser().parse(Files.readString(Paths.get(this.appConfiguration.getApplicationSchemaFolderLocation() + "\\" + this.appConfiguration.getSchema() + ".avsc")));
        	
        	String header = null;
        	
        	if (this.appConfiguration.getHeader() == true) { 
        		
        		header = Files.readString(Paths.get(this.appConfiguration.getApplicationHeaderFolderLocation() + "\\" + this.appConfiguration.getSchema() + ".json"));
        		
        	}        	
        	
        	String payload = Files.readString(Paths.get(this.appConfiguration.getApplicationPayloadFolderLocation() + "\\" + this.appConfiguration.getSchema() + ".json"));
        	
        	StopWatch sw = new StopWatch();
        	
        	sw.start();
        	
        	for (int b = 0; b < this.appConfiguration.getBatches(); b++) {
        		
            	List<CompletableFuture<SendResult<String, Record>>> listaFutures = new ArrayList<CompletableFuture<SendResult<String, Record>>>();
            	
            	for(int i = 0; i < this.appConfiguration.getEvents(); i++) {		
            		    				
                	JsonNode headerJsonNode = null;
                	
                	if (this.appConfiguration.getHeader() == true) {
                		
                    	headerJsonNode = mapper.readTree(substituirTokens(header));
                    	
                	}
                	                	
                	JsonNode payloadJsonNode = mapper.readTree(substituirTokens(payload));
            	              		
            		listaFutures.add(this.kafkaProducerService.produzir(topico, schema, headerJsonNode, payloadJsonNode));
            		
            	}
            	
            	CompletableFuture.allOf(listaFutures.toArray(new CompletableFuture[listaFutures.size()])).get();
        		
        	}
        	
        	sw.stop();
        	
        	log.info("Foram postados " + (this.appConfiguration.getBatches() * this.appConfiguration.getEvents()) + " eventos em " + sw.getTotalTimeSeconds() + " segundos, ficando " + (this.appConfiguration.getBatches() * this.appConfiguration.getEvents()) / sw.getTotalTimeSeconds() + " evt/s.");
        	
        	this.kafkaProducerService.printarMetricas();
    		
    	}
    	
    }
    
    public String substituirTokens(String jsonString) {
    	    	
    	return jsonString
    			.replace("{UUID}", UUID.randomUUID().toString())
    			.replace("{DATE-FORMATO-ISO}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
				.replace("{DATE-FORMATO-YYYYMMDD}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.replace("{DATETIME-FORMATO-ISO}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    	
    }
    
}
