package hsnguyen.demo.springbootwebflux;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class) //to use JUnit5
@WebFluxTest
@ContextConfiguration(classes = {DemoControllerAndRouter.class, DemoHandler.class})
class DemoSpringBootWebFluxApplicationTests {
    private static final Logger log=LoggerFactory.getLogger(DemoSpringBootWebFluxApplicationTests.class);

	
    @Autowired
    private ApplicationContext context;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

	@Test
	void hello()
	{
		log.info("Requesting /hello ...");
        
        webTestClient.get()
        .uri("/hello")
        .accept(MediaType.TEXT_PLAIN)
        .exchange()
        .expectStatus().isOk()
        .expectBody(String.class)
        .value(response -> {
        	log.info("Response message: " + response);
        	Assertions.assertNotNull(response);
        });
	}

}
 