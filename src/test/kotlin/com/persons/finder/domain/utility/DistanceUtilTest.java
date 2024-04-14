package com.persons.finder.domain.utility;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceUtilTest {

	@Test
	public void calculateDistanceReturnsZeroForSameStartAndEnd() {
		// When
		final BigDecimal distance = DistanceUtil.calculateDistance(BigDecimal.valueOf(90), BigDecimal.valueOf(180),
				BigDecimal.valueOf(90), BigDecimal.valueOf(180));

		// Then
		assertEquals(0, distance.compareTo(BigDecimal.ZERO));
	}
}
