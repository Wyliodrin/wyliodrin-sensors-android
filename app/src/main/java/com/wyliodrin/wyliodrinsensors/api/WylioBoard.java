package com.wyliodrin.wyliodrinsensors.api;

import org.json.JSONObject;
import org.json.JSONException;

public class WylioBoard {
	
	public static final String WYLIO_ADDRESS = "https://www.wyliodrin.com/";
	
	private String communication_token;
	private String boardid;
	
	public WylioBoard (String communication_token)
	{
		this.communication_token = communication_token;
	}
	
	public WylioBoard (String communication_token, String boardid)
	{
		this (communication_token);
		this.boardid = boardid;
	}
	
	public void sendMessage (String label, String message)
	{
		if (boardid == null)
		{
			sendOpenMessage(this.communication_token, label, message);
		}
		else
		{
			sendMessage(this.communication_token, this.boardid, label, message);
		}
	}
	
	public void sendMessage (String label, double message)
	{
		if (boardid != null)
		{
			sendOpenMessage(this.communication_token, label, message);
		}
		else
		{
			sendMessage(this.communication_token, this.boardid, label, message);
		}
	}
	
	public void sendMessage (String label, JSONObject message)
	{
		if (boardid != null)
		{
			sendOpenMessage(this.communication_token, label, message);
		}
		else
		{
			sendMessage(this.communication_token, this.boardid, label, message);
		}
	}
	
	public static void sendMessage (String communication_token, String boardid, String label, String message)
	{
		final JSONObject json = new JSONObject();
        try {
            json.put("communication_token", communication_token);
            json.put("gadgetid", boardid);
            json.put("label", label);
            json.put("message", message);
            } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		sendOpenMessage(json);
	}
	
	public static void sendMessage (String communication_token, String boardid, String label, double message)
	{
		final JSONObject json = new JSONObject();
		try {
            json.put("communication_token", communication_token);
            json.put("gadgetid", boardid);
            json.put("label", label);
            json.put("message", message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		sendOpenMessage(json);
	}
	
	public static void sendMessage (String communication_token, String boardid, String label, JSONObject message)
	{
		final JSONObject json = new JSONObject();
        try {
            json.put("communication_token", communication_token);
            json.put("gadgetid", boardid);
            json.put("label", label);
            json.put("message", message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		sendOpenMessage(json);
	}
	
	public static void sendOpenMessage (String communication_token, String label, String message)
	{
		final JSONObject json = new JSONObject();
        try {
            json.put("communication_token", communication_token);
            json.put("label", label);
            json.put("message", message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		sendOpenMessage(json);
	}
	
	public static void sendOpenMessage (String communication_token, String label, double message)
	{
		final JSONObject json = new JSONObject();
        try {
            json.put("communication_token", communication_token);
            json.put("label", label);
            json.put("message", message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		sendOpenMessage(json);
	}
	
	public static void sendOpenMessage (String communication_token, String label, JSONObject message)
	{
		final JSONObject json = new JSONObject();
        try {
            json.put("communication_token", communication_token);
            json.put("label", label);
            json.put("message", message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		sendOpenMessage(json);
	}
	
	public static void sendOpenMessage (JSONObject json)
	{
		final String s = json.toString();
		System.out.println(s);
		new Thread ()
		{
			public void run ()
			{
				System.out.println("WylioBoard: sending post json");
				WylioIO.sendPostJson(WYLIO_ADDRESS+"message", s);
			}
		}.start ();
	}
}
