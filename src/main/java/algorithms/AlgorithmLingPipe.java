package algorithms;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

import controllers.KeywordController;

public class AlgorithmLingPipe implements AlgoritmosClasificacion {// clasifyerLoader
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmLingPipe.class);
	
	String[] categories;
	LMClassifier clasificador;

	public AlgorithmLingPipe() {

		try {
			clasificador = (LMClassifier) AbstractExternalizable.readObject(new File("classifier.txt"));
			categories = clasificador.categories();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public int clasificar(String tweet) {
		ConditionalClassification classification = clasificador.classify(tweet);
		// System.out.println(clasificador.classify(tweet));

		if (classification.bestCategory().equals("pos")) {
			return 3;

		} else {
			return 1;
		}
	}

}