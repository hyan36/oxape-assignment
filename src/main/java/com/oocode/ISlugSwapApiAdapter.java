package com.oocode;

import java.math.BigDecimal;

import com.teamoptimization.SlugSwaps.Timeout;

public interface ISlugSwapApiAdapter {

	void acceptCheapOdds(String p2p) throws Timeout;

	String getP2PQuote(int slugId, String raceName, BigDecimal targetOdds);

}