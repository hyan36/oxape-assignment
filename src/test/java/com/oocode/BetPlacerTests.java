package com.oocode;

import org.junit.Before;
import org.junit.Test;

import com.teamoptimization.Quote;
import com.teamoptimization.SlugSwaps;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when; 
import java.math.BigDecimal;

public class BetPlacerTests {

	protected Boolean accepted = false;

	protected Boolean agreed = false;
	
	protected String acceptedId = null;	

	protected int expensiveOdds = 100;

	protected BetPlacer betPlacer;
	
	protected Quote defaultQuote;
	
	
	protected ISlugRacingOddsApiAdapter slugRacingOddsAdapter = mock(ISlugRacingOddsApiAdapter.class);
	
	protected ISlugSwapApiAdapter slugSwapApiAdapter = mock(ISlugSwapApiAdapter.class);
	
	protected ITimer timer = mock(ITimer.class);

	@Before
	public void initialize() {
		defaultQuote = new Quote(new BigDecimal(100), "something");
		when(slugRacingOddsAdapter.getExpensiveOdds(002, "something")).thenReturn(defaultQuote);		
		when(timer.TimeOut(1000)).thenReturn(false);
		betPlacer = new BetPlacer(this.slugRacingOddsAdapter, this.slugSwapApiAdapter, timer);
	}

	@Test
	public void usesCheaperProviderIfOddsTheSame() throws Exception {
		acceptedId = "somethingrandomid";
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(100))).thenReturn(acceptedId);
		
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		verify(slugSwapApiAdapter).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter, never()).agreeExpensiveOdds(new Quote(new BigDecimal(expensiveOdds), "something"));
	}

	@Test
	public void usesCheaperProviderIfOddsBetter() throws Exception {
		acceptedId = "somethingrandomid";
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(102))).thenReturn(acceptedId);
		betPlacer.placeBet(002, "something", new BigDecimal(102));
		verify(slugSwapApiAdapter).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter, never()).agreeExpensiveOdds(new Quote(new BigDecimal(expensiveOdds), "something"));
	}

	@Test
	public void usesExpensiveProviderIfOddsBetter() throws Exception {
		acceptedId = "somethingrandomid";
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(100))).thenReturn(acceptedId);	
		
		expensiveOdds = 102;
		Quote quote = new Quote(new BigDecimal(expensiveOdds), "something");
		when(slugRacingOddsAdapter.getExpensiveOdds(002, "something")).thenReturn(quote);
		
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		verify(slugSwapApiAdapter, never()).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter).agreeExpensiveOdds(quote);
	}

	@Test
	public void placesExpensiveBetIfTargetOddsNotMetOnCheapBet() throws Exception {
		acceptedId = null;
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(100))).thenReturn(acceptedId);	
		
		expensiveOdds = 102;
		Quote quote = new Quote(new BigDecimal(expensiveOdds), "something");
		when(slugRacingOddsAdapter.getExpensiveOdds(002, "something")).thenReturn(quote);
		
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		verify(slugSwapApiAdapter, never()).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter).agreeExpensiveOdds(quote);
	}

	@Test
	public void placesNoBetIfTargetOddsNotMetOnEither() throws Exception {
		acceptedId = null;
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(102))).thenReturn(acceptedId);	
		
		expensiveOdds = 100;
		Quote quote = new Quote(new BigDecimal(expensiveOdds), "something");
		when(slugRacingOddsAdapter.getExpensiveOdds(002, "something")).thenReturn(quote);
		
		betPlacer.placeBet(002, "something", new BigDecimal(102));
		verify(slugSwapApiAdapter, never()).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter, never()).agreeExpensiveOdds(quote);
	}
	
	@Test
	public void testTimerTimeOut() throws Exception {
		acceptedId = "somethingrandomid";
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(102))).thenReturn(acceptedId);	
		
		expensiveOdds = 102;
		Quote quote = new Quote(new BigDecimal(expensiveOdds), "something");
		when(slugRacingOddsAdapter.getExpensiveOdds(002, "something")).thenReturn(quote);
		
		when(timer.TimeOut(1000)).thenReturn(true);		
		verify(slugSwapApiAdapter, never()).acceptCheapOdds(acceptedId);
		verify(slugRacingOddsAdapter, never()).agreeExpensiveOdds(quote);
	}
	
	@Test
	public void testSwapApiAcceptOddsTimeout()throws Exception {
		acceptedId = "somethingrandomid";
		when(slugSwapApiAdapter.getP2PQuote(002, "something", new BigDecimal(100))).thenReturn(acceptedId);
		doThrow(new SlugSwaps.Timeout()).when(slugSwapApiAdapter).acceptCheapOdds(acceptedId);
		
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		verify(slugRacingOddsAdapter).agreeExpensiveOdds(defaultQuote);
	}
	
}
