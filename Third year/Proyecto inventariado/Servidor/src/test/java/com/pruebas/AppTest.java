package com.pruebas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
class AppTest {
	@LocalServerPort
	private int port;

	@Test
	void shouldReturn200WhenSendingRequestToController() throws Exception {
	}

	@Test
	void shouldReturn200WhenSendingRequestToManagementEndpoint() throws Exception {
	}
}
