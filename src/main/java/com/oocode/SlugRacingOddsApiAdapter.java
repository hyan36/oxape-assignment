package com.oocode;

import com.teamoptimization.Quote;
import com.teamoptimization.SlugRacingOddsApi;

public class SlugRacingOddsApiAdapter implements ISlugRacingOddsApiAdapter {

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
	

}
