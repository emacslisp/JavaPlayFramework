package controllers;

import java.util.Date;
import java.util.UUID;

import common.api.MD5Generator;

import play.*;
import play.mvc.*;
import play.libs.*;

import static play.libs.Json.toJson;
import redis.clients.jedis.Jedis; 

import com.fasterxml.jackson.databind.JsonNode;

public class ApiTokenController extends Controller {
	
	public static MD5Generator generator = new MD5Generator();
	public static Jedis jedis = new Jedis("127.0.0.1"); 
	
	public static class UserInfo {
		public String userName;
		public String password;
	}
	
	public static class ResponseMessage {
		public boolean success;
		public String token;
		public String clientId;
		
		public ResponseMessage(boolean success,String token,String clientId) {
			this.success = success;
			this.token = token;
			this.clientId = clientId;
		}
	}
	
	public static class ResponseToken {
		public String message = "Bearer";
		public String token;
	}
	
	public Result registerClient() {
		
		JsonNode json = request().body().asJson();
		String clientId = json.get("client_id").asText();
		
		//@TODO: check clientId here
		String clientSecret = UUID.randomUUID().toString();
		
		//@TODO: save clientId to with clientSecret
		//
		
		return ok(toJson(new ResponseMessage(true,clientSecret,clientId)));
	}
	
	public Result tokenAuthorization() {
		
		JsonNode json = request().body().asJson();

		String client_id = json.get("client_id").asText();
		String secret = json.get("secret").asText();

		// @TODO: check user security
		// CheckUserSecurity

		String accessToken = "";
		try {
			if (jedis.get(client_id) != null) {
				accessToken = jedis.get(client_id);
			} else {
				accessToken = generator.generateValue();
				jedis.set(accessToken, client_id);
				jedis.set(client_id, accessToken);
				jedis.expire(accessToken, 3600);
				jedis.expire(client_id, 3600);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ok(toJson(new ResponseMessage(false, accessToken, client_id)));
		}

		return ok(toJson(new ResponseMessage(true,accessToken,client_id)));
	}
	
	public Result access () {
		String token = request().getHeader("Authorization");
		JsonNode json = request().body().asJson();
		//String token = json.get("access_token").asText();
		
		System.out.println(token);
		
		String[] tokenBody = token.split(" ");
		String hashToken = tokenBody[1];
		
		if(!tokenBody[0].equals("Bearer")) {
			return ok(toJson(new ResponseMessage(false,hashToken,"")));
		}
		
		if(jedis.get(hashToken) == null) {
			return ok(toJson(new ResponseMessage(false,hashToken,jedis.get(hashToken))));
		}
		
		return ok(toJson(new ResponseMessage(true,hashToken,jedis.get(hashToken))));
	}
}
