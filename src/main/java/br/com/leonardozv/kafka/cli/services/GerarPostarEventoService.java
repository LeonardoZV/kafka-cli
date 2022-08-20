package br.com.leonardozv.kafka.cli.services;

import java.io.IOException;
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

	private final AppConfiguration appConfiguration;

	private final KafkaProducerService kafkaProducerService;

	@Autowired
	public GerarPostarEventoService(AppConfiguration appConfiguration, KafkaProducerService kafkaProducerService) {
		this.appConfiguration = appConfiguration;
		this.kafkaProducerService = kafkaProducerService;
	}

    public void gerarPostarEvento(String topico, Schema schema, String tokenizedHeader, String tokenizedKey, String tokenizedValue) throws IOException, InterruptedException {

    	ObjectMapper mapper = new ObjectMapper();

		CountDownLatch countDownLatch = new CountDownLatch(Math.toIntExact(this.appConfiguration.getEvents()));
    	
        for (long e = 1; e <= this.appConfiguration.getEvents(); e++) {
        	
        	JsonNode headerJsonNode = null;
        	
        	if (tokenizedHeader != null) {
            	headerJsonNode = mapper.readTree(substituirTokens(tokenizedHeader));
        	}

			String key = null;

			if (tokenizedKey != null) {
				key = substituirTokens(tokenizedKey);
			}

			String value = null;

			if (tokenizedValue != null) {
				value = substituirTokens(tokenizedValue);
			}

        	this.kafkaProducerService.produzir(topico, schema, headerJsonNode, key, value).whenComplete((r,t) -> countDownLatch.countDown());
        	
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
