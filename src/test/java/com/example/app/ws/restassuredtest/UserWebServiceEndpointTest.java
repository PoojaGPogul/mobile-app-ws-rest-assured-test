package com.example.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserWebServiceEndpointTest {

	private final String CONTEXT_PATH = "/mobile-app-ws";
	private final String EMAIL_ADDRESS = "poojapogul@gmail.com";
	private final String JSON = "application/json";
	private static String userId;
	private static String authorizationHeader;
	private static List<Map<String, String>> addresses;

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	/*
	 * testUserLogin()
	 *
	 */
	@Test
	final void a() {
		Map<String, String> loginDetails = new HashMap<String, String>();
		loginDetails.put("email", EMAIL_ADDRESS);
		loginDetails.put("password", "123");

		Response response = given().contentType(JSON).accept(JSON).body(loginDetails).when()
				.post(CONTEXT_PATH + "/users/login").then().statusCode(200).extract().response();

		authorizationHeader = response.header("Authorization");
		assertNotNull(authorizationHeader);

		userId = response.header("userID");
		assertNotNull(userId);
		assertTrue(userId.length() == 30);

	}

	/*
	 * testGetUserDetails()
	 *
	 */
	@Test
	final void b() {
		Response response = given().pathParam("id", userId)
				.header("Authorization", authorizationHeader).accept(JSON)
				.when().get(CONTEXT_PATH + "/users/{id}")
				.then().statusCode(200).contentType(JSON).extract().response();

		String userPublicId = response.jsonPath().getString("userId");
		String email = response.jsonPath().getString("email");
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");

		assertNotNull(userPublicId);
		assertNotNull(email);
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals(EMAIL_ADDRESS, email);

		addresses = response.jsonPath().getList("addresses");
		assertNotNull(addresses);
		assertTrue(addresses.size() == 1);

		String addressId = addresses.get(0).get("addressId");
		assertNotNull(addressId);
		assertTrue(addressId.length() == 30);

	}

	/**
	 * testUpdateUserDetails()
	 */
	@Test
	final void c() {
		Map<String, Object> userDetails = new HashMap<String, Object>();
		userDetails.put("firstName", "ABC");
		userDetails.put("lastName", "XYZ");		
		
		Response response = given().contentType(JSON).accept(JSON)
				.header("Authorization", authorizationHeader)
				.pathParam("id", userId)
				.body(userDetails)
				.when().put(CONTEXT_PATH + "/users/{id}")
				.then().statusCode(200)
				.contentType(JSON).extract().response();
		
		
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals("ABC", firstName);
		assertEquals("XYZ", lastName);
		
		List<Map<String, String>> storedAddresses = response.jsonPath().getList("addresses");
		assertNotNull(storedAddresses);
		assertEquals(addresses.size(), storedAddresses.size());
		assertEquals(addresses.get(0).get("streetName"), storedAddresses.get(0).get("streetName"));
		
	}
	
	/**
	 * testDeleteUserDetails
	 * */
	@Test
	final void d() {
		Response response = given().header("Authorization", authorizationHeader)
		.pathParam("id", userId)
		.when().delete(CONTEXT_PATH+"/users/{id}")
		.then().statusCode(200).contentType(JSON).extract().response();
		
		String result = response.jsonPath().getString("operationResult");
		assertNotNull(result);
		assertEquals("SUCCESS", result);
		
	}

}
