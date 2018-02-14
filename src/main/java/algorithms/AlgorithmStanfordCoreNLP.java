package algorithms;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DBHashTag;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

//import edu.stanford.nlp.rnn.RNNCoreAnnotations;

public class AlgorithmStanfordCoreNLP implements AlgoritmosClasificacion{

	private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmStanfordCoreNLP.class);
	
		
	private String toCss(int sentiment) {
        switch (sentiment) {
        case 0:
            return "Very negative 0";
        case 1:
            return "Negative 1";
        case 2:
            return "Neutral 2";
        case 3:
            return "Positive 3";
        case 4:
            return "Very positive 4";
        default:
            return "";
        }
}
	
	@Override
	public int clasificar(String tweet) {
		LOGGER.info("UNOOOOO");
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        int mainSentiment = 0;
        LOGGER.info("DOOOOOOS");
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                //Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
            	Tree tree = sentence.get(SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
 
            }
        }
        LOGGER.info("TREEEEES");
        if ( mainSentiment > 4 || mainSentiment < 0) {
            return -1;
            
        }
        LOGGER.info("CUATROOO");
        //System.out.println("Tweet: "+tweet+" \nSentiment: "+toCss(mainSentiment));
        return mainSentiment;
		
	}

}
