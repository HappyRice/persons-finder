package com.persons.finder.presentation;

import com.persons.finder.data.dto.CreatePersonRequest;
import com.persons.finder.data.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class PersonControllerTest {

	private static final String PERSONS_PATH = "/api/v1/persons";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	public void createPersonOK() {
		// When
		final ResponseEntity<Person> response = this.restTemplate.exchange(PERSONS_PATH,
				HttpMethod.POST, new HttpEntity<>(CreatePersonRequest.builder().withName("Bob").build()), Person.class);

		final Person person = response.getBody();

		// Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(person);
		assertEquals(1, person.getId());
		assertEquals("Bob", person.getName());
	}
}
