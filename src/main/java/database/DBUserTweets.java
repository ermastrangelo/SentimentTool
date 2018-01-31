package database;

import java.io.IOException;
import java.util.ArrayList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBUserTweets extends DataBase{

	public DBUserTweets(String fileName) {
		super(fileName);
	}
	
	@Override
	public void getTweets(String userName) {

		//ANDA DE 10! Baje 1400 y quiza se puede mas
		//FALTA: COMO SABER CUANTOS BAJAR
		
		Paging pg = new Paging();
		Twitter twitter = new TwitterFactory().getInstance();
		  int numberOfTweets = 1000;//VER COMO HACER PARAMETRIZABLE
		  long lastID = Long.MAX_VALUE;
		  ArrayList<Status> tweets = new ArrayList<Status>();
		  
		  while (tweets.size () < numberOfTweets) {
		    try {
		      tweets.addAll(twitter.getUserTimeline(userName,pg));
		      System.out.println("Gathered " + tweets.size() + " tweets");
		      for (Status t: tweets) 
		        if(t.getId() < lastID) lastID = t.getId();
		    }
		    catch (TwitterException te) {
		      System.out.println("Couldn't connect: " + te);
		    }; 
		    pg.setMaxId(lastID-1);
		  }
		  

		  for (Status t: tweets){
			  writeDb(t.getText());}
		  
		  System.out.println("CANTIDAD DESCARGADA "+tweets.size());
		
		}
}

