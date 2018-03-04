package com.oocode;

import org.junit.Assert;
import org.junit.Test;

import com.teamoptimization.Race;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when; 
import java.math.BigDecimal;

public class SlugSwapApiAdapterTest {
	
	SlugSwapApiAdapter adapter;
	
	Race race = mock(Race.class);
	
	
	@Test
	public void TestCanGetValidP2PQuote() {
		BigDecimal odds = new BigDecimal(100);
		when(race.quote(002, odds)).thenReturn("someid");
		adapter = new SlugSwapApiAdapter() {
			public Race getRace(String raceName) {
				return race;
			}			
		};
		String result = adapter.getP2PQuote(002, "something", odds);
		Assert.assertEquals("someid", result);		
	}
	
	@Test
	public void TestFailedToGetP2PQuote() {
		BigDecimal odds = new BigDecimal(100);
		when(race.quote(002, odds)).thenReturn("someid");
		adapter = new SlugSwapApiAdapter() {
			public Race getRace(String raceName) {
				return null;
			}			
		};
		String result = adapter.getP2PQuote(002, "something", odds);
		Assert.assertNull(result);	
		verify(race, never()).quote(002, odds);
	}

}
