package database;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;

public abstract class DataBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataBase.class);

	BufferedWriter bw = null;
	FileWriter fw = null;
	ExtractorTweets extractorT=new ExtractorTweets();
	ClasificadorDeSentimientos clasificador;

	public DataBase(ClasificadorDeSentimientos cl) {
		// constructor solo crea e inicializa el archivo
		try {
			fw = new FileWriter("TweetsDB");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			LOGGER.error("Creating TweetsDB : " + e.getMessage());
		}
		clasificador=cl;
	}

	public void writeDb(String tweet) {
		// preprocesa y guarda en la base de datos el tweet dado

		String tweetPreprocesado = extractorT.preProcesarTweet(tweet);
		
		try {
			if (tweetPreprocesado.length() > 1){
				bw.write(tweetPreprocesado + "\n");// bw.write(tweetPreprocesado+"
			}	// ");
		} catch (IOException e) {
			LOGGER.error("Writing line: " + e.getMessage());
		}
	}

	public void closeFile() {
		// cierra el archivo - DB
		try {
			bw.close();
		} catch (IOException e) {
			LOGGER.error("Closing TweetsDB file: " + e.getMessage());
		}
	}

	public abstract void getTweets(String userName,int cantBajar);

}
