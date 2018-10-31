package com.asitc.pgmicroservice;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.asitc.pgmicroservice.controller.error.model.ErrorResponseDTO;
import com.asitc.pgmicroservice.controller.user.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class IntegrationTest {

	private static HttpHeaders headers;
	private static HttpHeaders xmlHeaders;
	private static UserDTO user1;
	private static UserDTO user2;
	private static UserDTO user3;
	private static UserDTO CREATED_USER;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeClass
	public static void init() {
		IntegrationTest.headers = new HttpHeaders();
		IntegrationTest.headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		IntegrationTest.headers.setContentType(MediaType.APPLICATION_JSON);
		IntegrationTest.xmlHeaders = new HttpHeaders();
		IntegrationTest.xmlHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		IntegrationTest.xmlHeaders.setContentType(MediaType.APPLICATION_JSON);
		IntegrationTest.user1 = new UserDTO();
		IntegrationTest.user1.setEmail("test@domain.co.uk");
		IntegrationTest.user1.setFirstName("Test");
		IntegrationTest.user2 = new UserDTO();
		IntegrationTest.user2.setFirstName("Updated Test");
		IntegrationTest.user3 = new UserDTO();
		IntegrationTest.user3.setEmail("updated.test@domain.co.uk");
		IntegrationTest.user3.setFirstName("Updated Test");
	}

	@Test
	public void Test01_EndpointNotFound() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/unexisting-endpoint",
				HttpMethod.GET, entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test02_UserPostEndpoint_MethodNotAllowed() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user", HttpMethod.GET,
				entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test03_UserPostEndpoint_BadRequest() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user", HttpMethod.POST,
				entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test04_UserPostEndpoint_Ok() throws Exception {
		final HttpEntity<UserDTO> entity = new HttpEntity<>(IntegrationTest.user1, IntegrationTest.headers);
		final ResponseEntity<UserDTO> responseEntity = this.restTemplate.exchange("/api/user", HttpMethod.POST, entity,
				UserDTO.class);
		IntegrationTest.CREATED_USER = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(IntegrationTest.CREATED_USER.getId()).isNotNull();
		Assertions.assertThat(IntegrationTest.CREATED_USER.getEmail()).isEqualTo(IntegrationTest.user1.getEmail());
		Assertions.assertThat(IntegrationTest.CREATED_USER.getFirstName())
				.isEqualTo(IntegrationTest.user1.getFirstName());
	}

	@Test
	public void Test05_UserPutEndpoint_BadRequest() throws Exception {
		final HttpEntity<UserDTO> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/{id}",
				HttpMethod.PUT, entity, ErrorResponseDTO.class, IntegrationTest.CREATED_USER.getId());
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test06_UserPutEndpoint_BadRequestValidation() throws Exception {
		final HttpEntity<UserDTO> entity = new HttpEntity<>(IntegrationTest.user2, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/{id}",
				HttpMethod.PUT, entity, ErrorResponseDTO.class, IntegrationTest.CREATED_USER.getId());
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).contains("email may not be null");
	}

	@Test
	public void Test07_UserPutEndpoint_NotFound() throws Exception {
		final HttpEntity<UserDTO> entity = new HttpEntity<>(IntegrationTest.user3, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/0",
				HttpMethod.PUT, entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test08_UserPutEndpoint_Ok() throws Exception {
		final HttpEntity<UserDTO> entity = new HttpEntity<>(IntegrationTest.user3, IntegrationTest.headers);
		final ResponseEntity<String> responseEntity = this.restTemplate.exchange("/api/user/{id}", HttpMethod.PUT,
				entity, String.class, IntegrationTest.CREATED_USER.getId());
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	public void Test09_UserGetEndpoint_BadRequest() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/a",
				HttpMethod.GET, entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test10_UserGetEndpoint_NotAcceptable() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.xmlHeaders);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/{id}",
				HttpMethod.GET, entity, ErrorResponseDTO.class, IntegrationTest.CREATED_USER.getId());
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
	}

	@Test
	public void Test11_UserGetEndpoint_Ok() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<UserDTO> responseEntity = this.restTemplate.exchange("/api/user/{id}", HttpMethod.GET,
				entity, UserDTO.class, IntegrationTest.CREATED_USER.getId());
		final UserDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getId()).isEqualTo(IntegrationTest.CREATED_USER.getId());
	}

	@Test
	public void Test12_UserDeleteEndpoint_NotFound() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/0",
				HttpMethod.DELETE, entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test13_UserDeleteEndpoint_Ok() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<String> responseEntity = this.restTemplate.exchange("/api/user/{id}", HttpMethod.DELETE,
				entity, String.class, IntegrationTest.CREATED_USER.getId());
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	public void Test14_UserGetEndpoint_NotFound() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate.exchange("/api/user/{id}",
				HttpMethod.GET, entity, ErrorResponseDTO.class, IntegrationTest.CREATED_USER.getId());
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test15_UserSearchByFirstNameEndpoint_BadRequest() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<ErrorResponseDTO> responseEntity = this.restTemplate
				.exchange("/api/user/search/findByFirstName", HttpMethod.GET, entity, ErrorResponseDTO.class);
		final ErrorResponseDTO response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.getHttpCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		Assertions.assertThat(response.getHttpMessage()).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
		Assertions.assertThat(response.getMoreInformation()).isNotBlank();
	}

	@Test
	public void Test16_UserSearchByFirstNameEndpoint_OkNoResults() {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<UserDTO[]> responseEntity = this.restTemplate.exchange(
				"/api/user/search/findByFirstName?firstName={fn}", HttpMethod.GET, entity, UserDTO[].class,
				"NonExistingUser");
		final UserDTO[] response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.length).isEqualTo(0);
	}

	@Test
	public void Test17_UserSearchByNameEndpoint_Ok() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<UserDTO[]> responseEntity = this.restTemplate.exchange(
				"/api/user/search/findByFirstName?firstName={fn}", HttpMethod.GET, entity, UserDTO[].class, "User1");
		final UserDTO[] response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.length).isEqualTo(1);
	}

	@Test
	public void Test18_UserSearchByPatternEndpoint_OkNoResults() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<UserDTO[]> responseEntity = this.restTemplate.exchange(
				"/api/user/search/findByPattern?firstName={fn}", HttpMethod.GET, entity, UserDTO[].class,
				"nonexisting");
		final UserDTO[] response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.length).isEqualTo(0);
	}

	@Test
	public void Test19_UserSearchByPatternEndpoint_Ok() throws Exception {
		final HttpEntity<String> entity = new HttpEntity<>(null, IntegrationTest.headers);
		final ResponseEntity<UserDTO[]> responseEntity = this.restTemplate.exchange(
				"/api/user/search/findByPattern?firstName={fn}", HttpMethod.GET, entity, UserDTO[].class, "use");
		final UserDTO[] response = responseEntity.getBody();
		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		Assertions.assertThat(response.length).isEqualTo(7);
	}

}
