package controllers;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;
import database.ExtractorTweets;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//LLAMAR A BackEnd desde browser
//http://localhost:8080/keywords?id=121143434&user=mambo&cantBajar=500

@RestController
public class KeywordController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeywordController.class);
	
	@CrossOrigin(origins = "http://localhost:4200") 
	@RequestMapping("/keywords")

	public String getKeywords(@RequestParam Map<String, String> requestParams, HttpServletResponse response) {//probar de sacar response
		
		LOGGER.info(" -------------BEGIN KEYWORD CONTROLLER--------------");
		
		String id = requestParams.get("id");
		String user = requestParams.get("user");//equivale al keyword
		//String algorithm = requestParams.get("algorithm"); HARCODIE ALGORITMO 2
		String algorithm = "1";
		String cantBajarString = requestParams.get("cantBajar");
		int cantBajar = Integer.parseInt(cantBajarString);

		// instancio algoritmos
		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("2")) {
			algo = new AlgorithmStanfordCoreNLP();
			LOGGER.info("Algorithm StanfordCoreNLP created.");
		} else {
			algo = new AlgorithmLingPipe();
			LOGGER.info("Algorithm LingPipe created.");	
		}
		
		//instancio clasificador con el algoritmo
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);

		DataBase db;
		db = new DBHashTag(cl);
		LOGGER.info("Starting to get and clasify tweets with keyword: "+user+".");
		db.getTweets(user,cantBajar);
		db.closeFile();
		
		LOGGER.info("Finish get and clasify");
		LOGGER.info(" -------------END KEYWORD CONTROLLER--------------");
		
		
		return db.returnForQlik();		
	}

}