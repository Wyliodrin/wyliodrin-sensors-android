package com.wyliodrin.wyliodrinsensors.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class WylioIO {
	 
	private static final String USER_AGENT = "Mozilla/5.0";
 
	// HTTP GET request
	public static void sendGet(String url) {
 
		try
		{
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
//			System.out.println("Response Code : " + responseCode);
//	 
//			BufferedReader in = new BufferedReader(
//			        new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//	 
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
//	 
//			//print result
//			System.out.println(response.toString());
			if (responseCode != 200)
			{
				System.out.println("WylioIO: error for get request "+url);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
 
	// HTTP POST request
	public static void sendPostJson(String url, String json) {
		try
		{
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	 
			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/json");
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(json);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'POST' request to URL : " + url);
//			System.out.println("Post parameters : " + json);
//			System.out.println("Response Code : " + responseCode);
//	 
//			BufferedReader in = new BufferedReader(
//			        new InputStreamReader(con.getInputStream()));
//			String inputLine;
//			StringBuffer response = new StringBuffer();
//	 
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine);
//			}
//			in.close();
	 
			//print result
//			System.out.println(response.toString());
			if (responseCode != 200)
			{
				System.out.println("WylioIO: error for post request "+url+" "+json);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
 
	}
 
}
