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

	// Check if a String is Pure ascii
	public boolean isPureAscii(String str) {
		
		return asciiEncoder.canEncode(str);
	}

	public static void main(String[] args) {
		
		tweets_cleaned tweet = new tweets_cleaned();
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("./tweet_input/tweets.txt"));
			PrintWriter pw = new PrintWriter(new FileWriter("./tweet_output/ft1.txt"));
			
			String jsonLine;
			String text = null;
			String timestamp = null;
			int noOfLinewithUnicodeChar = 0;
					
			while((jsonLine = br.readLine())!=null){
				
				JSONObject obj = new JSONObject(jsonLine);
				
				// Get the text field from json tweet only if it is present
				if(obj.has("text")){
					
					// Remove all Escape characters from String line
					text = obj.getString("text").replaceAll("[\\n\\r\\t]", "").replaceAll("(?<=/)[^/]+?(?=/)", "");
					
					// Count and remove unicode characters
					if(!tweet.isPureAscii(text)){
						
						noOfLinewithUnicodeChar += 1;
						text = text.replaceAll("[^\\p{ASCII}]", "");
					}
				}
				
				// Get the created_at field from json tweet only if it is present
				if(obj.has("created_at")){
					timestamp = obj.getString("created_at");//.replaceAll("\\P{Print}", "");//.replaceAll("(?<=/)[^/]+?(?=/)", "");
				}
				
				System.out.println(text+" "+ "(timestamp: "+timestamp+")");
				// Write the cleaned output to output file ft1.txt
				pw.println(text+" "+ "(timestamp: "+timestamp+")");

			}
			pw.println("\n\n"+ noOfLinewithUnicodeChar + " tweets contained unicode.");

			// Write the unicode characters count output file ft1.txt
			System.out.println("\n"+ noOfLinewithUnicodeChar + " tweets contained unicode.");
			
			br.close();
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not found Exception: "+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception: "+ e.getMessage());
		}

	}
}