package algorithms;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

public class AlgorithmLingPipe implements AlgoritmosClasificacion {// clasifyerLoader
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