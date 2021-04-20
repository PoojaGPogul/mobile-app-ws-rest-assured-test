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
		Response response = given()
				.pathParam("id", userId)
				.header("Authorization", authorizationHeader).accept(JSON)
				.when().get(CONTEXT_PATH + "/users/{id}")
				.then().statusCode(200).contentType(JSON)
				.extract().response();
		
		String userPublicId = response.jsonPath().getString("userId");
		String email = response.jsonPath().getString("email");
		String firstName = response.jsonPath().getString("firstName");
		String lastName = response.jsonPath().getString("lastName");
		
		assertNotNull(userPublicId);
		assertNotNull(email);
		assertNotNull(firstName);
		assertNotNull(lastName);
		assertEquals(EMAIL_ADDRESS, email);
		
		List<Map<String, String>> addresses = response.jsonPath().getList("addresses");
		assertNotNull(addresses);
		assertTrue(addresses.size() == 1);
		
		String addressId = addresses.get(0).get("addressId");
		assertNotNull(addressId);
		assertTrue(addressId.length() == 30);
		
		
	}

}
