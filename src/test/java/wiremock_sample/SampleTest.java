package wiremock_sample;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.FileReaderUtil;

public class SampleTest extends BaseTest{
		
	@Test
	public void verifyGetBook200() {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest.queryParam("id","2")
				.header("Authorization", "Bearer thisIsBearer")
				.header("Content-Type", "application/json")
				.get("v1/getbook");
		
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), 200);
		Assert.assertEquals(res.body().jsonPath().getString("id"), "2");		
	}
	
	@Test
	public void verifyGetBook401() {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest.queryParam("id","2")
				.header("Authorization", "Bearer thisIsInvalidBearer")
				.header("Content-Type", "application/json")
				.get("v1/getbook");
		
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), 401);
		Assert.assertEquals(res.body().jsonPath().getString("message"), "You are not authorized to access this resource");		
	}
	
	@Test
	public void verifyAddBook200() throws IOException {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest
				.header("Authorization", "Bearer thisIsBearer")
				.header("Content-Type", "application/json")
				.body(FileReaderUtil.readResourceContent("postBody.json"))
				.post("v1/addbook");
		
		System.out.println(res.body().asString());
		
		Assert.assertEquals(res.statusCode(), 200);
		Assert.assertNotNull(res.body().jsonPath().getString("id"));
	}
	
	
	@Test
	public void verifyAddBook401() throws IOException {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest
				.header("Authorization", "Bearer thisIsInvalidBearer")
				.header("Content-Type", "application/json")
				.body(FileReaderUtil.readResourceContent("postBody.json"))
				.post("v1/addbook");
		
		System.out.println(res.body().asString());
		
		Assert.assertEquals(res.statusCode(), 401);
		Assert.assertEquals(res.body().jsonPath().getString("message"), "You are not authorized to access this resource");	
		
	}
	
	@Test
	public void verifyUpdateBook200() throws IOException {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest
				.header("Authorization", "Bearer thisIsBearer")
				.header("Content-Type", "application/json")
				.body(FileReaderUtil.readResourceContent("putBody.json"))
				.put("v1/updatebook");
		
		System.out.println(res.body().asString());
		
		Assert.assertEquals(res.statusCode(), 200);
	}

	@Test
	public void verifyUpdateBook401() throws IOException {
		//TODO
	}
	
	@Test
	public void calculateFineLessThan10Days() throws IOException {
		RestAssured.baseURI = hostURL;
		RequestSpecification httpRequest = RestAssured.given();
		Response res = httpRequest.header("Authorization", "Bearer thisIsBearer")
				.header("Content-Type", "application/json")
				.body(FileReaderUtil.readResourceContent("calcFineRequestLessThan10Days.json"))
				.post("/v1/calculatefine");
		
		System.out.println(res.body().asString());
		Assert.assertEquals(res.statusCode(), 200);
		Assert.assertEquals(res.body().jsonPath().getString("fine"), "50.00");
	}


}
