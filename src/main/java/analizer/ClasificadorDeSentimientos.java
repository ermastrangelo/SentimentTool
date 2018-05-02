package analizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgoritmosClasificacion;

public class ClasificadorDeSentimientos {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClasificadorDeSentimientos.class);

	BufferedReader br;
	AlgoritmosClasificacion algoritmh;

	public ClasificadorDeSentimientos(AlgoritmosClasificacion a) {
		this.algoritmh = a;
	}

	public void setAlgorithm(AlgoritmosClasificacion a) {
		this.algoritmh = a;
	}

	public String parsearSentimiento(int Sentimiento) {
		switch (Sentimiento) {
		case 0:
			return "Negativo";
		case 1:
			return "Negativo";
		case 2:
			return "Neutral";
		case 3:
			return "Positivo";
		case 4:
			return "Positivo";
		default:
			return "SENTIMIENTO RARO";
		}
		
	}

	public String clasificarTweets(String line) {

//		String fileName = "TweetsDB";
		int sentimiento = -1;
		String sentimientoString="";

		if ((line.length() != 0) && (line != "\n") && (line != " ")) {
			
			// mando a clasificar
			sentimiento = algoritmh.clasificar(line);
			
			sentimientoString=parsearSentimiento(sentimiento);
			

			// mostrarSentimiento(sentimiento);
		}
		
		return sentimientoString;
	}

}
