import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LastFMRecommenderTest {
	@Test
	public void LastFMRecommender() throws IOException {
		var recommender = new LastFMRecommender();
		recommender.listFiends(2);
		recommender.commonFriends(2, 124);
		recommender.listArtists(2, 4);
		recommender.listTop10();
		recommender.recommend10(2);
	}
}
