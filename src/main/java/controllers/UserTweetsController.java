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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//http://localhost:8080/usertweets?user=e_mastrangelo&algorithm=2

@RestController
public class UserTweetsController {

	@RequestMapping("/usertweets")
	public String getUserTweets(@RequestParam Map<String, String> requestParams) {

		String user = requestParams.get("user");
		String algorithm = requestParams.get("algorithm");
		
		AlgoritmosClasificacion algo = null;
		if (algorithm.equals("1")) {
			algo = new AlgorithmStanfordCoreNLP();
		} else {
			algo = new AlgorithmLingPipe();
		}
		
		DataBase db;
		db = new DBUserTweets();
		db.getTweets(user);
		db.closeFile();
		
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets();

		return "Hola Ing. Mastrángelo: getUserTweets finalizo exitosamente";
	}

}

