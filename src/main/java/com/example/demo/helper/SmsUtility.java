package com.example.demo.helper;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class SmsUtility {
	public static int sendSms(String recipient, String message){
		try {
			System.out.println("recipient: " + recipient);
//			HttpResponse<String> response =  Unirest.post("https://api.ultramsg.com/instance2903/messages/chat")
//					  .header("content-type", "application/x-www-form-urlencoded")
//					  .body("token=5furticrip40kqws&to=+91" + recipient + "&body=" + message + "&priority=1&referenceId=")
//					  .asString();
			
//			HttpResponse<String> response =  Unirest.post("https://api.ultramsg.com/instance3008/messages/chat")
//					  .header("content-type", "application/x-www-form-urlencoded")
//					  .body("token=du8b21hc5eepjnig&to=+91" + recipient + "&body=" + message + "&priority=1&referenceId=")
//					  .asString();
			
			HttpResponse<String> response = Unirest.post("https://api.ultramsg.com/instance3008/messages/chat")
					  .header("content-type", "application/x-www-form-urlencoded")
					  .body("token=du8b21hc5eepjnig&to=+91"+recipient+"&body="+message+"&priority=1&referenceId=")
					  .asString();
			
			return response.getStatus();
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			return -1;
		}
		
	}		
}
