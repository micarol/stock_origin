package com.micarol.stock.listener;

import java.nio.charset.StandardCharsets;

import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Component;

import com.micarol.stock.util.JsonUtil;
import com.micarol.stock.util.Loggers;
import com.micarol.stock.util.SendMail;

@Component
public class MailListener {

	public void sendMail(byte[] msg) {
		sendMail(new String(msg, StandardCharsets.UTF_8));
    }
	
	private void sendMail(String msg) {
		try {
			JsonNode json = JsonUtil.readTree(msg);
			SendMail.sendFileMail(json.get("to").asText(), json.get("from").asText(), json.get("subject").asText(), json.get("body").asText());
		} catch (Exception e) {
			Loggers.ERROR_LOG.error("sendMail err, message:{}", msg);
			Loggers.ERROR_LOG.error(e.getMessage(), e);
		}
		
	}
}
