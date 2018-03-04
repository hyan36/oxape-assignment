package com.oocode;

import java.math.BigDecimal;

import com.teamoptimization.Race;
import com.teamoptimization.SlugSwapsApi;
import com.teamoptimization.SlugSwaps.Timeout;

public class SlugSwapApiAdapter implements ISlugSwapApiAdapter {

	/* (non-Javadoc)
	 * @see com.oocode.ISlugSwapApiAdapter#acceptCheapOdds(java.lang.String)
	 */
	@Override
	public void acceptCheapOdds(String p2p) throws Timeout {
		SlugSwapsApi.accept(p2p);
	}
	

	public Race getRace(String raceName) {
		return SlugSwapsApi.forRace(raceName);
	}


	/* (non-Javadoc)
	 * @see com.oocode.ISlugSwapApiAdapter#getP2PQuote(int, java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public String getP2PQuote(int slugId, String raceName, BigDecimal targetOdds) {
		String result;
		Race race = getRace(raceName);
        if (race == null) {
            result = null;
        } else {
            result = race.quote(slugId, targetOdds);
        }
		return result;
	}



}
