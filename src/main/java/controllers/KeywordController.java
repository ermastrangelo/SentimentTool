package controllers;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//LLAMAR A QLIK DIRECTAMENTE??
//http://localhost:8080/keywords?keyword=mambo&algorithm=2&cantBajar=500

@RestController
public class KeywordController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeywordController.class);
	
	@RequestMapping("/keywords")
	public String getKeywords(@RequestParam Map<String, String> requestParams) {
		
		LOGGER.info(" -------------BEGIN KEYWORD CONTROLLER--------------");

		String keyword = requestParams.get("keyword");
		String algorithm = requestParams.get("algorithm");
		String cantBajarString = requestParams.get("cantBajar");
		int cantBajar = Integer.parseInt(cantBajarString);

		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("1")) {
			algo = new AlgorithmStanfordCoreNLP();
			LOGGER.info("Algorithm StanfordCoreNLP created.");
		} else {
			algo = new AlgorithmLingPipe();
			LOGGER.info("Algorithm LingPipe created.");
			
		}

		DataBase db;
		db = new DBHashTag();
		LOGGER.info("Starting to get tweets with keyword: "+keyword+".");
		db.getTweets(keyword,cantBajar);
		db.closeFile();
		
		
		LOGGER.info("Starting to clasify tweets.");		
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();
		LOGGER.info("Finish Clasification");
		LOGGER.info(" -------------END KEYWORD CONTROLLER--------------");
		return "Hola Ing. Mastrángelo: getKeywords finalizo exitosamente Feriado";

	}

}