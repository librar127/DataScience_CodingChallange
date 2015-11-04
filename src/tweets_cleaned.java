package InsightDataScience_coding_challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.json.JSONObject;

public class tweets_cleaned {
	
	CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); 
	static int noOfLinewithUnicodeChar = 0;

	/**
	 * This method is used to check if a String is Pure ASCII
	 * 
	 * @param 	str 	Input string provided to check if it contains unicode characters
	 * @return  boolean Returns true in case string is pure ASCII else false
	 */
	public boolean isPureAscii(String str) {
		
		return asciiEncoder.canEncode(str);
	}

	public static void main(String[] args) {
		
		if(args.length < 1){
			System.out.println("Usage: provide filename containing tweets");
			System.exit(0);
		}
		
		tweets_cleaned tc = new tweets_cleaned();
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			PrintWriter pw = new PrintWriter(new FileWriter("./tweet_output/ft1.txt"));
			
			String jsonLine;
			String[] cleanedTweet;
					
			while((jsonLine = br.readLine())!=null){

				cleanedTweet = tc.cleanTweets(jsonLine);
				
				if(cleanedTweet[0] != null && cleanedTweet[1] != null)				
					pw.println(cleanedTweet[0]+" (timestamp: "+cleanedTweet[1]+")");

			}
			
			pw.println("\n\n"+ noOfLinewithUnicodeChar + " tweets contained unicode.");

			/**
			 * Write the unicode characters count output file ft1.txt
			 */
			System.out.println("Output written to tweet_output/ft1.txt"+"\n"+ noOfLinewithUnicodeChar + " tweets contained unicode.");
			
			br.close();
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not found Exception: "+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception: "+ e.getMessage());
		}

	}
	
	/**
	 * This methods cleans tweets after removing escape characters and unicode characters and then separating text and timestamp.
	 * 
	 * @param tweetLine One Single Line of tweets
	 * @return List of cleaned Text and timestamp after separating them from tweetline
	 */
	
	private String[] cleanTweets(String tweetLine) {
		
		tweets_cleaned tc = new tweets_cleaned();
		
		JSONObject obj = new JSONObject(tweetLine);
		String text = null;
		String timestamp = null;
		String[] textAndTimestamp = new String[2];
		
		/**
		 * Get the text field from json tweet only if it is present
		 */
		if(obj.has("text")){
			
			/**
			 * Remove all Escape characters from String line
			 */
			//text = obj.getString("text").replaceAll("[\\n\\t]", " ").replaceAll("(?<=/)[^/]+?(?=/)", "");
										/**
										 * \/ -> /
										 */
			text = obj.getString("text").replaceAll("\\\\/", "\\/")					
										 /**
										  * \\ -> \
										  */
										.replaceAll("\\\\\\\\", "\\\\")
										/**
										 * ==  \' -> '
										 */
										.replaceAll("\\\\'", "\'")	
										/**
										 * ==  \n & \t -> " " (Single Space)
										 */
										.replaceAll("[\\n\\t]", " ");
																		
			
			/**
			 * Count and remove unicode characters
			 */
			if(!tc.isPureAscii(text)){		
				
				noOfLinewithUnicodeChar += 1;
				text = text.replaceAll("[^\\p{ASCII}]", "");
			}
		}
		
		/**
		 * Get the created_at field from json tweet only if it is present
		 */
		if(obj.has("created_at")){
			timestamp = obj.getString("created_at").replaceAll("(?<=/)[^/]+?(?=/)", "");
		}

		textAndTimestamp[0] = text;
		textAndTimestamp[1] = timestamp;
		
		return textAndTimestamp;
	}

}
