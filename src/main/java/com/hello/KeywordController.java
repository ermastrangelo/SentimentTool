package com.hello;

import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//LLAMAR A QLIK DIRECTAMENTE??

@RestController
public class KeywordController {

    @RequestMapping("/keywords")
    //public String getKeywords(@RequestParam(value="keyword") String keyword,@RequestParam(value="algorithm") String algorithm) {
    public String getKeywords(@RequestParam Map<String,String> requestParams){
    	
    	String keyword= requestParams.get("keyword");
    	String algorithm= requestParams.get("algorithm");
    	
    	AlgoritmosClasificacion algo=null;
    	
    	if (algorithm.equals("1"))
    	{
    		algo= new AlgorithmStanfordCoreNLP();    		
    	}else
    	{
    		algo=new AlgorithmLingPipe();
    	}
    	
		DataBase db;
		db = new DBHashTag("TweetsDB");
		db.getTweets(keyword);		
		db.closeFile();
    	
		
		ClasificadorDeSentimientos cl = new ClasificadorDeSentimientos(algo);
		cl.clasificarTweets("TweetsDB"); 	   	
    	
    	
    	
    	
    	return "Hola Ing. Mastrángelo: Felicitaciones por tu primer servicio rest";
 
    }

}