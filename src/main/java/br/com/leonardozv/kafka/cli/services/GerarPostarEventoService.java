package br.com.leonardozv.kafka.cli.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import br.com.leonardozv.kafka.cli.config.AppConfiguration;
import org.apache.avro.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GerarPostarEventoService {

//	private static Logger log = LoggerFactory.getLogger(GerarPostarEventoService.class);
	
	@Autowired
	private AppConfiguration appConfiguration;
	
	@Autowired
	private KafkaProducerService kafkaProducerService;

    public void gerarPostarEvento(String topico, Schema schema, String header, String payload) throws Exception {

    	ObjectMapper mapper = new ObjectMapper();

		CountDownLatch countDownLatch = new CountDownLatch(Math.toIntExact(this.appConfiguration.getEvents()));
    	
        for (long e = 1; e <= this.appConfiguration.getEvents(); e++) {
        	
        	JsonNode headerJsonNode = null;
        	
        	if (this.appConfiguration.getHeader()) {
            	headerJsonNode = mapper.readTree(substituirTokens(header));
        	}
        	
        	this.kafkaProducerService.produzir(topico, schema, headerJsonNode, substituirTokens(payload)).whenComplete((r,t) -> countDownLatch.countDown() );
        	
        }

		countDownLatch.await();

    }
    
    public String substituirTokens(String jsonString) {
    	
    	return jsonString
    			.replace("{UUID}", UUID.randomUUID().toString())
    			.replace("{DATE-FORMATO-ISO}", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
				.replace("{DATE-FORMATO-YYYYMMDD}", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
				.replace("{DATETIME-FORMATO-ISO}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    	
    }
    
//    List<CompletableFuture<SendResult<String, Record>>> listaFutures = new ArrayList<CompletableFuture<SendResult<String, Record>>>();
//    
//	for (int i = 0; i < this.appConfiguration.getEvents(); i++) {
//	
//JsonNode headerJsonNode = null;
//
//if (this.appConfiguration.getHeader() == true) {
//
//headerJsonNode = mapper.readTree(substituirTokens(header));
//
//}
//	
//JsonNode payloadJsonNode = mapper.readTree(substituirTokens(payload));
//
//this.kafkaProducerService.produzir(topico, schema, headerJsonNode, payloadJsonNode);
//
//listaFutures.add(this.kafkaProducerService.produzir(topico, schema, headerJsonNode, payloadJsonNode));
//
//}
//
//log.info("Aguardando a finalização da transmissão.");
//
//CompletableFuture.allOf(listaFutures.toArray(new CompletableFuture[listaFutures.size()])).get();
    
}
