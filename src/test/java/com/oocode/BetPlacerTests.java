package com.oocode;

import org.junit.Before;
import org.junit.Test;

import com.teamoptimization.Quote;

import org.junit.*;

import java.math.BigDecimal;

public class BetPlacerTests {

	protected Boolean accepted;

	protected Boolean agreed;
	
	protected String acceptedId;

	protected BetPlacer betPlacer;
	
	protected int expensiveOdds;

	@Before
	public void initialize() {
		accepted = false;
		agreed = false;
		acceptedId = null;
		expensiveOdds = 100;
		betPlacer = new BetPlacer() {
			@Override
			public String getP2PQuote(int slugId, String raceName, BigDecimal targetOdds) {
				return acceptedId;
			}
			@Override
			public void agreeExpensiveOdds(Quote b) {
				agreed = true;
			}
			@Override
			public Quote getExpensiveOdds(int slugId, String raceName) {
				return new Quote(new BigDecimal(expensiveOdds), raceName);
			}
			@Override
			public void acceptCheapOdds(String p2p) {
				accepted = true;
			}
		};
	}

	@Test
	public void usesCheaperProviderIfOddsTheSame() throws Exception {
		acceptedId = "somethingrandomid";
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		Assert.assertEquals(false, agreed);
		Assert.assertEquals(true, accepted);
	}

	@Test
	public void usesCheaperProviderIfOddsBetter() throws Exception {
		acceptedId = "somethingrandomid";
		betPlacer.placeBet(002, "something", new BigDecimal(102));
		Assert.assertEquals(false, agreed);
		Assert.assertEquals(true, accepted);
	}

	@Test
	public void usesExpensiveProviderIfOddsBetter() throws Exception {
		acceptedId = null;
		expensiveOdds = 102;
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		Assert.assertEquals(true, agreed);
		Assert.assertEquals(false, accepted);
	}

	@Test
	public void placesExpensiveBetIfTargetOddsNotMetOnCheapBet() throws Exception {
		acceptedId = null;
		expensiveOdds = 102;
		betPlacer.placeBet(002, "something", new BigDecimal(100));
		Assert.assertEquals(true, agreed);
		Assert.assertEquals(false, accepted);
	}

	@Test
	public void placesNoBetIfTargetOddsNotMetOnEither() throws Exception {
		acceptedId = null;
		expensiveOdds = 100;
		betPlacer.placeBet(002, "something", new BigDecimal(102));
		Assert.assertEquals(false, agreed);
		Assert.assertEquals(false, accepted);
	}
}
