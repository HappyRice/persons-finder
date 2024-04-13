package com.persons.finder.data.transformer;

import com.persons.finder.data.dto.LocationDto;
import com.persons.finder.data.model.Location;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocationTransformerTest {

	@Test
	public void buildLocationDtoPadsToSixDecimalPlaces() {
		// Given
		final Location location = Location.builder()
				.withLatitude(BigDecimal.valueOf(90))
				.withLongitude(BigDecimal.valueOf(180))
				.build();

		// When
		final LocationDto locationDto = LocationTransformer.buildLocationDto(location);

		// Then
		assertNotNull(locationDto);
		assertEquals("90.000000", locationDto.getLatitude().toString());
		assertEquals("180.000000", locationDto.getLongitude().toString());
	}
}
