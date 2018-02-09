package controllers;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DBReplyTweets;
import database.DataBase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//http://localhost:8080/replies?id=958406889288716290&user=LeoDiCaprio&algorithm=2

@RestController
public class RepliesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RepliesController.class);

	@RequestMapping("/replies")
	public String getReplies(@RequestParam Map<String, String> requestParams) {

		String id = requestParams.get("id");
		String user = requestParams.get("user");
		String algorithm = requestParams.get("algorithm");
		
		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("1")) {
			algo = new AlgorithmStanfordCoreNLP();
			LOGGER.info("Algorithm StanfordCoreNLP created.");
		} else {
			algo = new AlgorithmLingPipe();
			LOGGER.info("Algorithm LingPipe created.");
		}
		
		DataBase db;
		db = new DBReplyTweets(id);
		LOGGER.info("Starting to get replies from : "+user+" tweet.");
		db.getTweets(user);
		db.closeFile();

		LOGGER.info("Starting to clasify tweets.");
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();
		LOGGER.info("Finish Clasification");
		
		
		
		
		return "Hola Ing. Mastrángelo: getReplies finalizo exitosamente";
	}

}
