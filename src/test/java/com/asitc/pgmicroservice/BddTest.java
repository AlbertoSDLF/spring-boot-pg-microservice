package com.asitc.pgmicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.asitc.pgmicroservice.controller.user.UserDTO;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class BddTest {

    private final String USER_ID_ENDPOINT = "/api/user/{id}";
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    protected ResponseEntity<UserDTO> getUserById(final Long id) {
        return this.restTemplate.getForEntity(this.USER_ID_ENDPOINT, UserDTO.class, id);
    }
}
