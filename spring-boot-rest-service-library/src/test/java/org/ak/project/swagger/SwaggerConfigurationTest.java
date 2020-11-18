package org.ak.project.swagger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerConfigurationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testSwaggerRedirect() throws URISyntaxException {

        ResponseEntity<String> response = testRestTemplate.getForEntity("/", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.FOUND));

        assertEquals(response.getHeaders().getLocation(), new URI("http://localhost:" + port + "/swagger-ui.html"));



    }

    @Test
    void testCustomCssDownload() {

        ResponseEntity<String> response = testRestTemplate.getForEntity("/webjars/springfox-swagger-ui/swagger-ui.css", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("2d80d3"));

        assertEquals(MediaType.parseMediaType("text/css;charset=UTF-8"), response.getHeaders().getContentType());
    }

}
