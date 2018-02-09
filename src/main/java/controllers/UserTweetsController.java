package controllers;


import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DBReplyTweets;
import database.DBUserTweets;
import database.DataBase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//http://localhost:8080/usertweets?user=e_mastrangelo&algorithm=2

@RestController
public class UserTweetsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserTweetsController.class);

	@RequestMapping("/usertweets")
	public String getUserTweets(@RequestParam Map<String, String> requestParams) {

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
		db = new DBUserTweets();
		LOGGER.info("Starting to get tweets from: "+user+".");
		db.getTweets(user);
		db.closeFile();
		
		LOGGER.info("Starting to clasify tweets.");	
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();
		LOGGER.info("Finish Clasification");

		return "Hola Ing. Mastrángelo: getUserTweets finalizo exitosamente";
	}

}

