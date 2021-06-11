import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class ArtistsTest {
	@Test
	public void parseUserArtists() throws IOException {
		assertEquals(0, Artists.parseUserArtists(new StringReader("")).size());
		{
			assertThrows(ParseException.class,
				() -> Artists.parseUserArtists(new StringReader("a\n2"))
			);
		}
		{
			assertThrows(ParseException.class,
				() -> Artists.parseUserArtists(new StringReader("a\n2\t51"))
			);
		}
		{
			var s = "userID\tartistID\tweight\n2\t51\t13883";
			var expected = new HashMap<>(Map.of(2, new HashMap<>(Map.of(51, 13883))));
			assertEquals(expected, Artists.parseUserArtists(new StringReader(s)));
		}
		{
			var file = Files.newBufferedReader(Path.of("user_artists.dat"));
			var result = Artists.parseUserArtists(file);
			assertEquals(1892, result.size());
			assertEquals(50, result.get(2).size());
		}
	}

	@Test
	public void parseArtistNames() throws IOException {
		assertEquals(0, Artists.parseArtistNames(new StringReader("")).size());
		{
			assertThrows(ParseException.class,
				() -> Artists.parseArtistNames(new StringReader("a\n2"))
			);
		}
		{
			var s = "id\tname\turl\tpictureURL\n1\tMALICE MIZER";
			var expected = new HashMap<>(Map.of(1, "MALICE MIZER"));
			assertEquals(expected, Artists.parseArtistNames(new StringReader(s)));
		}
		{
			var file = Files.newBufferedReader(Path.of("artists.dat"));
			var result = Artists.parseArtistNames(file);
			assertEquals(17632, result.size());
			assertEquals("MALICE MIZER", result.get(1));
		}
	}

	@Test
	public void commonArtists() {
		{
			var artists = new Artists(new HashMap<>(), new HashMap<>());
			assertTrue(artists.commonArtists(0, 0).isEmpty());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0, new HashMap<>(Map.of(0, 1)))),
				new HashMap<>()
			);
			assertThrows(RuntimeException.class, () -> artists.commonArtists(0, 0));
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(2, 0)),
				1,
				new HashMap<>(Map.of(2, 0))
			)),
				new HashMap<>(Map.of(2, "name"))
			);
			assertEquals(Collections.singletonList("name"), artists.commonArtists(0, 1).get());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(2, 0, 3, 0)),
				1,
				new HashMap<>(Map.of(2, 0))
			)),
				new HashMap<>(Map.of(2, "name"))
			);
			assertEquals(Collections.singletonList("name"), artists.commonArtists(0, 1).get());
		}
	}

	@Test
	public void artistIDToString() {
		assertTrue(Artists.artistIDToString(new HashMap<>(), new ArrayList<>()).isEmpty());
		{
			assertThrows(RuntimeException.class,
				() -> Artists.artistIDToString(new HashMap<>(), Collections.singletonList(0))
			);
		}
		{
			var map = new HashMap<>(Map.of(0, "a"));
			var result = Artists.artistIDToString(map, Collections.singletonList(0));
			assertEquals(Collections.singletonList("a"), result);
		}
	}

	@Test
	public void top10ArtistNames() {
		{
			var artists = new Artists(new HashMap<>(), new HashMap<>());
			assertTrue(artists.top10ArtistNames().isEmpty());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0, new HashMap<>(Map.of(1, 1)))),
				new HashMap<>(Map.of(1, "name"))
			);
			assertEquals(Collections.singletonList("name"), artists.top10ArtistNames());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1)),
				1,
				new HashMap<>(Map.of(2, 2))
			)),
				new HashMap<>(Map.of(1, "a", 2, "b"))
			);
			assertEquals(Arrays.asList("b", "a"), artists.top10ArtistNames());
		}
	}

	@Test
	public void listenCountsOfUsers() {
		assertEquals(new HashMap<>(),
			Artists.listenCountsOfUsers(new HashMap<>(), new ArrayList<>()).get()
		);
		{
			var result = Artists.listenCountsOfUsers(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1))
			)), Collections.singletonList(0)).get();
			assertEquals(new HashMap<>(Map.of(1, 1)), result);
		}
		{
			// All users
			var result = Artists.listenCountsOfUsers(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1)),
				1,
				new HashMap<>(Map.of(1, 2))
				)),
				Arrays.asList(0, 1)
			).get();
			assertEquals(new HashMap<>(Map.of(1, 3)), result);
		}
		{
			// One user
			var result = Artists.listenCountsOfUsers(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1)),
				1,
				new HashMap<>(Map.of(1, 2))
				)),
				Collections.singletonList(0)
			).get();
			assertEquals(new HashMap<>(Map.of(1, 1)), result);
		}
		{
			// User not found
			var userArtists = new HashMap<>(Map.of(0, new HashMap<>(Map.of(1, 1))));
			assertTrue(Artists.listenCountsOfUsers(userArtists,
				Collections.singletonList(1)
			).isEmpty());
		}
	}

	@Test
	public void top10ArtistsOfUsers() {
		{
			var artists = new Artists(new HashMap<>(), new HashMap<>());
			assertTrue(artists.top10ArtistsOfUsers(new ArrayList<>()).get().isEmpty());
		}
		{
			var artists = new Artists(new HashMap<>(), new HashMap<>());
			var users = new ArrayList<>(Collections.singletonList(0));
			assertTrue(artists.top10ArtistsOfUsers(users).isEmpty());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0, new HashMap<>(Map.of(1, 1)))),
				new HashMap<>(Map.of(1, "name"))
			);
			var users = new ArrayList<>(Collections.singletonList(0));
			assertEquals(
				Collections.singletonList("name"),
				artists.top10ArtistsOfUsers(users).get()
			);
		}
	}
}
