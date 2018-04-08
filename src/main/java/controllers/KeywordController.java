package controllers;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;

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

//LLAMAR A QLIK DIRECTAMENTE??
//http://localhost:8080/keywords?id=121143434&user=mambo&cantBajar=500

@RestController
public class KeywordController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KeywordController.class);
	
	@CrossOrigin(origins = "http://localhost:4200") 
	@RequestMapping("/keywords")
	//@ResponseBody------------->>>Sacar?
	//HttpServletResponse
	public String getKeywords(@RequestParam Map<String, String> requestParams, HttpServletResponse response) {
		
		LOGGER.info(" -------------BEGIN KEYWORD CONTROLLER--------------");

		String id = requestParams.get("id");
		String user = requestParams.get("user");//equivale al keyword
		//String algorithm = requestParams.get("algorithm"); HARCODIE ALGORITMO 2
		String algorithm = "2";
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
		LOGGER.info("Starting to get tweets with keyword: "+user+".");
		db.getTweets(user,cantBajar);
		db.closeFile();
		
		
		LOGGER.info("Starting to clasify tweets.");		
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();
		LOGGER.info("Finish Clasification");
		LOGGER.info(" -------------END KEYWORD CONTROLLER--------------");
		
		
		//SACAR

		//File file = new File("ExampleTweets_1.csv");
		
//		Path path = Paths.get("ExampleTweets_1");
//		byte[] data = null;
//		try {
//			data = Files.readAllBytes(path);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			IOUtils.copy(new ByteArrayInputStream(data), response.getOutputStream());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		response.setHeader("Content-Disposition", "attachment;filename=ExampleTweets_1.csv");

        //return response;
		//.................
		
		
		//return file.getAbsoluteFile();
		//return "Hola Ing. ERomanM: getKeywords finalizo exitosamente Llamada por Front End";
		//return "{ key1: 'value1', key2: 'value2' }";
		return "TEXT	RETWEETS	COMMENTS	SENTIMENT	DATE	LOCATION	CITY	LIKES	USR	GENDER\r\n" + 
				"Happy for all3 the chrismas gifts	0	0	Positive	01/01/2018	ARG	ROSARIO	21	Eli	F\r\n" + 
				"i hate war a lot of people is sad	5	26	Negative	01/01/2017	FRA	PARIS	64	Eric	F\r\n" + 
				"i hate war a lot of people is sad	5	26	Negative	01/01/2017	FRA	PARIS	64	Eric	F\r\n" + 
				"i hate war a lot of people is sad	5	26	Negative	01/01/2017	FRA	PARIS	64	Eric	F\r\n" + 
				"";
		
	}

}