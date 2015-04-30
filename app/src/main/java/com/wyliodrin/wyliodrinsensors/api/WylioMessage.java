package com.wyliodrin.wyliodrinsensors.api;

import org.json.JSONObject;

public interface WylioMessage {
	public void receivedMessage(String from, String label, String message);
	public void receivedMessage(String from, String label, JSONObject message);
}
