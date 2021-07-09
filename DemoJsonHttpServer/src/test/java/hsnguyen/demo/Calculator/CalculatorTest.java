/*******************************************************************************
 * Copyright (c) 2021 Nguyen, Howie S. (howiesynguyen@gmail.com)
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ===========================================================
 * Note: This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example may ignore some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 *******************************************************************************/
package hsnguyen.demo.Calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CalculatorTest {
	private static Logger log = LoggerFactory.getLogger(CalculatorTest.class.getName());

	private static CalculationServer calculationServer;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		calculationServer = CalculationServer.getInstance();
		calculationServer.startServer();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		calculationServer.shutdownServer();
	}

	@Test
	void test() throws IOException {
		//Notice: this is not truly a test unit since don't use any assert method. 
		//It's just a convenient way to check the code with the naked eye
		
		try(CloseableHttpClient client = HttpClients.createDefault()){
			
		    HttpPost httpPost = new HttpPost("http://localhost:8081/calculator");
		    httpPost.setHeader("Accept", "application/json");
		    httpPost.setHeader("Content-type", "application/json");
		    
		    String jsonResult;
		    JSONObject jo;
		    
		    jsonResult = submitARequestAndPrintResponse(client, httpPost, "{\"operator\":\"" + Calculator.OPERATOR_ADDITION + "\", \"a\":" + 1 + ", \"b\":" + 2 + "}");
			assertNotNull(jsonResult);
			jo = new JSONObject(jsonResult);
			float floatResult = jo.getFloat("result");
			assertEquals(floatResult, 3.0);
			
			jsonResult = submitARequestAndPrintResponse(client, httpPost, "{\"operator\":\"" + Calculator.OPERATOR_DIVISION + "\", \"a\":" + 1 + ", \"b\":" + 0 + "}");
			assertNotNull(jsonResult);
			jo = new JSONObject(jsonResult);
			String result = jo.getString("result");
			assertTrue(result.contains("Error"));

		}

	}
	
	private String submitARequestAndPrintResponse(CloseableHttpClient client, HttpPost httpPost, String json) throws ClientProtocolException, IOException
	{
		String result;
		
    	log.info("Request: " + json);

	    httpPost.setEntity(new StringEntity(json));		
	    try (CloseableHttpResponse response = client.execute(httpPost)) {
	    	log.info("Response status code: " + response.getStatusLine().getStatusCode());
	    	result = new String(response.getEntity().getContent().readAllBytes());
	    	log.info(result + "\n");
		}
	    
	    return result;
	}

}
