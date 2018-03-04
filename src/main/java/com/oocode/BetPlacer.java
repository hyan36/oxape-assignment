package com.oocode;

import com.teamoptimization.*;
import com.teamoptimization.SlugSwaps.Timeout;

import java.math.BigDecimal;

public class BetPlacer implements ISlugRacingOddsApiAdapter, ISlugSwapApiAdapter {
	
	ISlugRacingOddsApiAdapter slugRacingOddsApi;
	
	ISlugSwapApiAdapter slugSwapApi;
	
	public BetPlacer( ) {
		this.slugRacingOddsApi = this;
		this.slugSwapApi = this;
	}
	
	public BetPlacer(ISlugRacingOddsApiAdapter slugRacingOddsApi, ISlugSwapApiAdapter slugSwapApi) {
		this.slugRacingOddsApi = slugRacingOddsApi;
		this.slugSwapApi = slugSwapApi;
	}

    public static void main(String[] args) throws Exception {
        /* Results usually look like a bit like one of the following:
           Time out on SlugSwaps
           accepted quote = 14281567-1fde-4996-a61f-0ba60b2c95c0 with offered odds 0.87
           accepted quote = dada5f35-c244-4da6-a370-648ea35f7a03 with required odds 0.50
        */

        // Note that the names of today’s races change every day!
        new BetPlacer().placeBet(3, "The Monday race", new BigDecimal("0.50"));
    }

    public void placeBet(int slugId, String raceName, BigDecimal targetOdds) {
        String result;
        result = this.slugSwapApi.getP2PQuote(slugId, raceName, targetOdds);
        String p2p = result;
        Quote b = this.slugRacingOddsApi.getExpensiveOdds(slugId, raceName);
        if (p2p != null && targetOdds.compareTo(b.odds) >= 0) {
            try {
            	this.slugSwapApi.acceptCheapOdds(p2p);
            } catch (SlugSwaps.Timeout timeout) {
            }
        } else {
            if (b.odds.compareTo(targetOdds) >= 0) {
            	this.slugRacingOddsApi.agreeExpensiveOdds(b);
            }
        }
    }


	/* (non-Javadoc)
	 * @see com.oocode.ISlugRacingOddsApiAdapter#agreeExpensiveOdds(com.teamoptimization.Quote)
	 */
	@Override
	public void agreeExpensiveOdds(Quote b) {
		SlugRacingOddsApi.agree(b.uid);
	}
	
	/* (non-Javadoc)
	 * @see com.oocode.ISlugRacingOddsApiAdapter#getExpensiveOdds(int, java.lang.String)
	 */
	@Override
	public Quote getExpensiveOdds(int slugId, String raceName) {
		return SlugRacingOddsApi.on(slugId, raceName);
	}
	
	/* (non-Javadoc)
	 * @see com.oocode.ISlugSwapApiAdapter#acceptCheapOdds(java.lang.String)
	 */
	@Override
	public void acceptCheapOdds(String p2p) throws Timeout {
		SlugSwapsApi.accept(p2p);
	}


	/* (non-Javadoc)
	 * @see com.oocode.ISlugSwapApiAdapter#getP2PQuote(int, java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public String getP2PQuote(int slugId, String raceName, BigDecimal targetOdds) {
		String result;
		Race race = SlugSwapsApi.forRace(raceName);
        if (race == null) {
            result = null;
        } else {
            result = race.quote(slugId, targetOdds);
        }
		return result;
	}
}
