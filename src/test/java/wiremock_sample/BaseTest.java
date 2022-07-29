package wiremock_sample;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;

import java.io.IOException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import utils.FileReaderUtil;

public class BaseTest {
	
	private int wireMockPort = 9110;
	private String wireMockHost = "localhost";
	private String bearerToken = "Bearer thisIsBearer";
	private String invalidBearerToken = "Bearer thisIsInvalidBearer";
	private WireMockServer wireMockServer;
	private WireMock wireMock;
	
	public String hostURL = "http://"+ wireMockHost +":"+ wireMockPort;
	
    @BeforeTest
    public void setup() throws InterruptedException, IOException {
    	
        System.out.println("********* Running Before Test ****************");
		wireMockServer = new WireMockServer(wireMockPort);
		wireMock = new WireMock(wireMockHost, wireMockPort);
		wireMockServer.start();
		
		wireMock.register(WireMock.get(WireMock.urlPathMatching("/v1/getbook"))
				.withQueryParam("id", WireMock.matching("([0-9]*)"))
				.withHeader("Authorization", equalTo(bearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("getBookResponse.json")));
		
		wireMock.register(WireMock.get(WireMock.urlPathMatching("/v1/getbook"))
				.withQueryParam("id", WireMock.matching("([0-9]*)"))
				.withHeader("Authorization", equalTo(invalidBearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.willReturn(aResponse().withStatus(401)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("unauthorizedResponse.json")));
		
		wireMock.register(WireMock.post(WireMock.urlPathMatching("/v1/addbook"))
				.withHeader("Authorization", equalTo(bearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson(FileReaderUtil.readResourceContent("postBody.json")))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("postBookResponse.json")));
		
		wireMock.register(WireMock.post(WireMock.urlPathMatching("/v1/addbook"))
				.withHeader("Authorization", equalTo(invalidBearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson(FileReaderUtil.readResourceContent("postBody.json")))
				.willReturn(aResponse().withStatus(401)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("unauthorizedResponse.json")));
		
		wireMock.register(WireMock.put(WireMock.urlPathMatching("/v1/updatebook"))
				.withHeader("Authorization", equalTo(bearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson("{\"id\": \"2\", \"title\": \"Mechanical Harry\", \"author\": \"Bob Kerr\", \"publisher\": \"Mallinson Rendel\", \"country\": \"NZ\"}"))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("putBookResponse.json")));
		
		wireMock.register(WireMock.put(WireMock.urlPathMatching("/v1/updatebook"))
				.withHeader("Authorization", equalTo(invalidBearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson("{\"id\": \"2\", \"title\": \"Mechanical Harry\", \"author\": \"Bob Kerr\", \"publisher\": \"Mallinson Rendel\", \"country\": \"NZ\"}"))
				.willReturn(aResponse().withStatus(401)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("unauthorizedResponse.json")));
		
		wireMock.register(WireMock.post(WireMock.urlPathMatching("/v1/calculatefine"))
				.withHeader("Authorization", equalTo(bearerToken))
				.withHeader("Content-Type", equalTo("application/json"))
				.withRequestBody(equalToJson(FileReaderUtil.readResourceContent("calcFineRequestLessThan10Days.json")))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json")
				.withBodyFile("calcFineResponseLessThan10Days.json")));

    }
     
    @AfterTest
    public void tiredown() {
    	wireMockServer.stop();
    }
 
}
