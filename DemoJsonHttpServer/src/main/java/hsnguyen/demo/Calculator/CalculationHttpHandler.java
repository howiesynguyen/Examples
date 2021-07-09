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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CalculationHttpHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		OutputStream outputStream = httpExchange.getResponseBody();

		httpExchange.getResponseHeaders().set("Content-Type", "appication/json");

		String response;
		if ("POST".equals(httpExchange.getRequestMethod())) {
			
			//read content from request
			InputStream inputStream = httpExchange.getRequestBody();
			String requestBody = new String(inputStream.readAllBytes());
			
			try
			{
				//convert to an JSon object
				JSONObject jo = new JSONObject(requestBody);
				String op = jo.getString("operator");
				float a = jo.getFloat("a");
				float b = jo.getFloat("b");
				
				try
				{
					float floatResult = Calculator.calculate2ParamsOperation(op, a, b);
					
					response = "{\"result\":"+ floatResult + ", \"operator\":\"" + op + "\", \"a\":" + a + ", \"b\":" + b + "}"; 
				}
				catch(ArithmeticException e)
				{
					response = "{\"result\":\"Error: " + e.getMessage() + "\", \"operator\":\"" + op + "\", \"a\":" + a + ", \"b\":" + b + "}"; 
				}

				
				httpExchange.sendResponseHeaders(200, response.length());

				outputStream.write(response.getBytes());
				outputStream.flush();
				outputStream.close();
			}
			catch(JSONException jsonException)
			{
				//bad request
				httpExchange.sendResponseHeaders(400, 0); 
			}

		}
		else {
			//Not support method 
			httpExchange.sendResponseHeaders(405, 0);
		}

	}

}
