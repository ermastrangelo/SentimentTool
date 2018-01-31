package database;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBReplyTweets extends DataBase{

	public DBReplyTweets(String fileName) {
		super(fileName);
		
	}

	@Override
	public void getTweets(String userName) {
		//ANDA DE 10
		//FALTA: RESOLVER COMO PASARLE EL ID del tweet
				
		Twitter twitter = TwitterFactory.getSingleton();
		
		long tweetId=956518245154148352L;

	    
	    Query query = new Query("[to:"+userName+"]");
	    query.count(100);
	    QueryResult result=null;
	    
	    int coments=1;	    
	    //int numBajados=1;
	    boolean finish=false;
	    
	    
	    while(!(finish)&&(coments!=0)){
	    	result=null;
	    	
	    	try {
	    		result = (twitter.search(query));

	    	} catch (TwitterException e) {

	    		e.printStackTrace();
	    	}
	    	
	    	coments = 0;
	    	
			if (result != null) {
				
				for (Status status : result.getTweets()) {

					if (status.getInReplyToStatusId() == tweetId) {
						
						writeDb(status.getText());
						coments++;
					}

				}
				
				if (result.nextQuery()!=null){
					query = result.nextQuery();
				}else{
					finish=true;
				}					
	    }
	    
	}

}
	
}
