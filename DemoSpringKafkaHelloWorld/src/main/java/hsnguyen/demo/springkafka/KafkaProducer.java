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
package hsnguyen.demo.springkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static final String TOPIC = "helloworld";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        logger.info("Producing message: " + message);
        kafkaTemplate.send(TOPIC, message);
    }
}
