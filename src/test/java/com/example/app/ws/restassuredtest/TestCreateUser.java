package com.example.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

class TestCreateUser {

	private final String CONTEXT_PATH = "/mobile-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	void test() {
		
		List<Map<String, Object>> userAddresses = new ArrayList<Map<String,Object>>();
		
		Map<String, Object> shippingAddress = new HashMap<String, Object>();
		shippingAddress.put("city", "Solapur");
		shippingAddress.put("country", "India");
		shippingAddress.put("streetName", "Street A");
		shippingAddress.put("postalCode", "123456");
		shippingAddress.put("type", "shipping");
		
		userAddresses.add(shippingAddress);
		
		Map<String , Object> userDetails = new HashMap<String, Object>();
		userDetails.put("firstName", "Pooja");
		userDetails.put("lastName", "Pogul");
		userDetails.put("email", "test@test.com");
		userDetails.put("password", "123");
		userDetails.put("addresses", userAddresses);
		
		
		Response response = given().contentType("application/json")
				.accept("application/json").body(userDetails)
				.when().post(CONTEXT_PATH + "/users")
				.then().statusCode(200).contentType("application/json")
				.extract().response();

	}

}