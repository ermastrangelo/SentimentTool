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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//http://localhost:8080/usertweets?id=876596756&user=e_mastrangelo&cantBajar=500

@RestController
public class UserTweetsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserTweetsController.class);
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/usertweets")
	
	public String getUserTweets(@RequestParam Map<String, String> requestParams) {

		LOGGER.info(" -------------BEGIN USER TWEETS CONTROLLER--------------");
		
		String id = requestParams.get("id");
		String user = requestParams.get("user");
		//String algorithm = requestParams.get("algorithm"); HARCODIE ALGORITMO 2
		String algorithm = "2";
		String cantBajarString = requestParams.get("cantBajar");
		int cantBajar = Integer.parseInt(cantBajarString);
		
		// instancio algoritmos
		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("1")) {
			algo = new AlgorithmStanfordCoreNLP();
			LOGGER.info("Algorithm StanfordCoreNLP created.");
		} else {
			algo = new AlgorithmLingPipe();
			LOGGER.info("Algorithm LingPipe created.");
		}
		
		//instancio clasificador con el algoritmo
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		
		DataBase db;
		db = new DBUserTweets(cl);
		LOGGER.info("Starting to get and clasify tweets from: "+user+".");
		db.getTweets(user,cantBajar);
		db.closeFile();

		LOGGER.info("Finish Clasification");
		LOGGER.info(" -------------END USER TWEETS CONTROLLER--------------");
		
		
		return db.returnForQlik();
		
	}

}

