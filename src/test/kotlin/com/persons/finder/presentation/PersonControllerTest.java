package com.persons.finder.presentation;

import com.persons.finder.data.dto.CreatePersonRequest;
import com.persons.finder.data.dto.LocationDto;
import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerTest {

	private static final String PERSONS_PATH = "/api/v1/persons";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	@Order(1)
	public void createPersonOK() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"id\": 1,\n" +
				"    \"name\": \"Bob\"\n" +
				"}";

		// When
		final HttpEntity<CreatePersonRequest> payload= new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("Bob")
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH,
				HttpMethod.POST, payload, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(2)
	public void getPersonOK() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"id\": 1,\n" +
				"    \"name\": \"Bob\"\n" +
				"}";

		// When
		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1",
				HttpMethod.GET, null, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(3)
	public void addUpdateLocationForPersonWithNoLocationOK() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"id\": 1,\n" +
				"    \"name\": \"Bob\",\n" +
				"    \"location\": {\n" +
				"        \"latitude\": 90.000000,\n" +
				"        \"longitude\": 180.000000\n" +
				"    }\n" +
				"}";

		// When
		final HttpEntity<LocationDto> payload= new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(90))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/location",
				HttpMethod.PUT, payload, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(4)
	public void addUpdateLocationForPersonWithExistingLocationOK() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"id\": 1,\n" +
				"    \"name\": \"Bob\",\n" +
				"    \"location\": {\n" +
				"        \"latitude\": 80.000000,\n" +
				"        \"longitude\": 170.000000\n" +
				"    }\n" +
				"}";

		// When
		final HttpEntity<LocationDto> payload= new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(80))
						.withLongitude(BigDecimal.valueOf(170))
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/location",
				HttpMethod.PUT, payload, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(5)
	public void getPeopleWithinRadiusWithNoOneAroundReturnsEmptyList() throws JSONException {
		// Expected
		final String expected = "[]";

		// Given
		// Create Jane with no location
		final HttpEntity<CreatePersonRequest> createPersonPayload = new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("Jane")
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH, HttpMethod.POST, createPersonPayload, String.class);

		// When
		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/vicinity?radiusInKm=500",
				HttpMethod.GET, null, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(6)
	public void getPeopleWithinRadiusWithSomeoneTooFarAndSomeoneWithNoLocationReturnsEmptyList() throws JSONException {
		// Expected
		final String expected = "[]";

		// Given
		// Create James
		final HttpEntity<CreatePersonRequest> createPersonPayload = new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("James")
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH, HttpMethod.POST, createPersonPayload, String.class);

		final HttpEntity<LocationDto> addLocationPayload = new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(89))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH + "/3/location", HttpMethod.PUT, addLocationPayload, String.class);

		// When
		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/vicinity?radiusInKm=500",
				HttpMethod.GET, null, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	@Order(7)
	public void getPeopleWithinRadiusWithPeopleAroundReturnsListOfPersonsSortedByDistance() throws JSONException {
		// Expected
		final String expected = "[\n" +
				"    {\n" +
				"        \"id\": 4,\n" +
				"        \"name\": \"John\",\n" +
				"        \"location\": {\n" +
				"            \"latitude\": 85.000000,\n" +
				"            \"longitude\": 180.000000\n" +
				"        }\n" +
				"    },\n" +
				"    {\n" +
				"        \"id\": 5,\n" +
				"        \"name\": \"Jim\",\n" +
				"        \"location\": {\n" +
				"            \"latitude\": 86.000000,\n" +
				"            \"longitude\": 180.000000\n" +
				"        }\n" +
				"    },\n" +
				"    {\n" +
				"        \"id\": 3,\n" +
				"        \"name\": \"James\",\n" +
				"        \"location\": {\n" +
				"            \"latitude\": 89.000000,\n" +
				"            \"longitude\": 180.000000\n" +
				"        }\n" +
				"    }\n" +
				"]";

		// Given
		// Create John
		HttpEntity<CreatePersonRequest> createPersonPayload = new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("John")
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH, HttpMethod.POST, createPersonPayload, String.class);

		HttpEntity<LocationDto> addLocationPayload = new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(85))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH + "/4/location", HttpMethod.PUT, addLocationPayload, String.class);

		// Create Jim
		createPersonPayload = new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("Jim")
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH, HttpMethod.POST, createPersonPayload, String.class);

		addLocationPayload = new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(86))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);
		this.restTemplate.exchange(PERSONS_PATH + "/5/location", HttpMethod.PUT, addLocationPayload, String.class);

		// When
		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/vicinity?radiusInKm=500000000",
				HttpMethod.GET, null, String.class);

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void getMissingPersonReturns404() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Person was not found\"\n" +
				"}";

		// When
		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/123",
				HttpMethod.GET, null, String.class);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void addLocationForMissingPersonReturns404() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Person was not found\"\n" +
				"}";

		// When
		final HttpEntity<LocationDto> payload= new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(90))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/123/location",
				HttpMethod.PUT, payload, String.class);

		// Then
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void createPersonWithNameExceeding50CharactersReturns400() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Name must not exceed 50 characters;\"\n" +
				"}";

		// When
		final HttpEntity<CreatePersonRequest> payload= new HttpEntity<>(
				CreatePersonRequest.builder()
						.withName("Bobbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH,
				HttpMethod.POST, payload, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void createPersonWithEmptyPayloadReturns400() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Invalid payload\"\n" +
				"}";

		// When
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH,
				HttpMethod.POST, new HttpEntity<>(headers), String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void addUpdateLocationWithLongitudeTooLargeReturns400() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Longitude should not exceed 180;\"\n" +
				"}";

		// When
		final HttpEntity<LocationDto> payload= new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(90))
						.withLongitude(BigDecimal.valueOf(200))
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/location",
				HttpMethod.PUT, payload, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}

	@Test
	public void addUpdateLocationWithValueMoreThan6DecimalPlacesReturns400() throws JSONException {
		// Expected
		final String expected = "{\n" +
				"    \"success\": false,\n" +
				"    \"message\": \"Latitude should not exceed 6 decimal places;\"\n" +
				"}";

		// When
		final HttpEntity<LocationDto> payload= new HttpEntity<>(
				LocationDto.builder()
						.withLatitude(BigDecimal.valueOf(80.9999999))
						.withLongitude(BigDecimal.valueOf(180))
						.build()
		);

		final ResponseEntity<String> response = this.restTemplate.exchange(PERSONS_PATH + "/1/location",
				HttpMethod.PUT, payload, String.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNotNull(response.getBody());
		JSONAssert.assertEquals(expected, response.getBody(), true);
	}
}
