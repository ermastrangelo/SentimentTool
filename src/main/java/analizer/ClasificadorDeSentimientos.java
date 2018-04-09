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

	public void mostrarSentimiento(int Sentimiento) {
		switch (Sentimiento) {
		case 0:
			System.out.println("Very negative 0");
			break;
		case 1:
			System.out.println("Negative 1");
			break;
		case 2:
			System.out.println("Neutral 2");
			break;
		case 3:
			System.out.println("Positive 3");
			break;
		case 4:
			System.out.println("Very positive 4");
			break;
		default:
			System.out.println("SENTIMIENTO RARO");
			break;
		}
	}

	public void clasificarTweets() {

		String fileName = "TweetsDB";
		int sentimiento = -1;

		//solo toma una linea, la envia a clasificar y guarda el resultado
		try {
			br = new BufferedReader(new FileReader(fileName));

			String line = br.readLine();

			while (line != null) {

				if ((line.length() != 0) && (line != "\n") && (line != " ")) {
					// mando a clasificar

					sentimiento = algoritmh.clasificar(line);

					// almaceno en algun archivo csv

					// mostrarSentimiento(sentimiento);
				}
				line = br.readLine();

			}

			br.close();

		} catch (FileNotFoundException e) {
			LOGGER.error("Opening TweetsDB 1: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Opening TweetsDB 1: " + e.getMessage());
		}
	}

}
