package com.oocode;

import com.teamoptimization.Quote;

public interface ISlugRacingOddsApiAdapter {

	void agreeExpensiveOdds(Quote b);

	Quote getExpensiveOdds(int slugId, String raceName);

}