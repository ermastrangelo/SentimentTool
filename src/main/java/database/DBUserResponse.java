package database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analizer.ClasificadorDeSentimientos;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBUserResponse extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBUserResponse.class);


	public DBUserResponse(ClasificadorDeSentimientos cl) {
		super(cl);
	}

	@Override
	public void getTweets(String userName, int cantBajar) {
		// poner a cantBajar=0 para que baje todos

		// ANDA DE 10
		// FALTA: RESOLVER COMO PASARLE EL ID del tweet
		// 956518245154148352L
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query("[to:" + userName + "]");
		query.count(100);
		query.setLang("en");//Lenguaje Ingles
		QueryResult result = null;

		int coments = 1;
		int numBajados = 0;
		boolean finish = false;

		while (!(finish) && (coments != 0)) {
			result = null;

			try {
				result = (twitter.search(query));

			} catch (TwitterException e) {

				LOGGER.error("Couldn't connect Twitter API: " + e.getMessage());
			}

			coments = 0;
			String line="";
			if (result != null) {

				for (Status status : result.getTweets()) {	
					
						
//					if (status.getInReplyToStatusId()==-1) {
						System.out.println("IN REPLIE TO: "+status.getInReplyToStatusId());
						
						line=armarLineaCSV(status);
						if(line.length()>2) {
							writeDb(line);
							numBajados++;
							coments++;
						}
//					}
						
						if (numBajados == cantBajar) {
							LOGGER.info("Downloaded tweets: " + numBajados + ".");
							return;
						}	

				}

				if (result.nextQuery() != null) {
					query = result.nextQuery();
				} else {
					finish = true;
				}

				if ((cantBajar != 0) && (numBajados >= cantBajar)) {
					finish = true;
				}

			}

		}
		LOGGER.info("Downloaded tweets: " + numBajados + ".");

	}

}