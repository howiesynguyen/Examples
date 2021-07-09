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
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpServer;

public class CalculationServer {
	private static Logger log = LoggerFactory.getLogger(CalculationServer.class.getName());
	
	public static final int PORT = 8081;
	public static final String PATH_CALCULATOR = "/calculator";
	
	private static CalculationServer _instance; 
	
	private ThreadPoolExecutor threadPoolExecutor;
	HttpServer server;

	
	public CalculationServer() throws IOException
	{
		threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    	server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
	}
	
	public void startServer() throws IOException
	{
    	server.createContext(PATH_CALCULATOR, new CalculationHttpHandler());
    	server.setExecutor(threadPoolExecutor);
    	server.start();
    	
    	log.info("Task Scheduling Server started on port " + PORT);
	}
	
	
	public void shutdownServer() throws IOException
	{
		if(threadPoolExecutor != null)
			threadPoolExecutor.shutdown();
		
		if(server != null)
			server.stop(0);

    	log.info("Task Scheduling Server stopped");
	}

	
    public static void main(String[] args) throws IOException {
    	CalculationServer.getInstance().startServer();
    }
    
    public static CalculationServer getInstance() throws IOException {
        if (_instance == null)
        {
        	_instance = new CalculationServer();
        }
    	return _instance;
    }

}
