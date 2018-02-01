package com.hello;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DBReplyTweets;
import database.DataBase;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//http://localhost:8080/replies?id=958406889288716290&user=LeoDiCaprio&algorithm=2

@RestController
public class RepliesController {

	@RequestMapping("/replies")
	public String getReplies(@RequestParam Map<String, String> requestParams) {

		String id = requestParams.get("id");
		String user = requestParams.get("user");
		String algorithm = requestParams.get("algorithm");
		
		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("1")) {
			algo = new AlgorithmStanfordCoreNLP();
		} else {
			algo = new AlgorithmLingPipe();
		}
		
		DataBase db;
		db = new DBReplyTweets(id);
		db.getTweets(user);
		db.closeFile();
		
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();
		
		
		
		
		return "Hola Ing. Mastrángelo: getReplies finalizo exitosamente";
	}

}
