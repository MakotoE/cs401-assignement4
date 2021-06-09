import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistsTest {
	@Test
	public void parseUserArtists() throws IOException {
		assertEquals(0, Artists.parseUserArtists(new StringReader("")).size());
		{
			var s = "userID\tartistID\tweight\n2\t51\t13883";
			var expected = new HashMap<>(Map.of(2,
				new HashSet<>(Set.of(new Artists.ArtistInfo(51, 13883)))
			));
			assertEquals(expected, Artists.parseUserArtists(new StringReader(s)));
		}
		{
			var file = Files.newBufferedReader(Path.of("user_artists.dat"));
			var result = Artists.parseUserArtists(file);
			assertEquals(1892, result.size());
			assertEquals(50, result.get(2).size());
		}
	}
}
