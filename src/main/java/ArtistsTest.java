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
			assertArrayEquals(new String[]{"name"}, artists.commonArtists(0, 1).get());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(2, 0, 3, 0)),
				1,
				new HashMap<>(Map.of(2, 0))
			)),
				new HashMap<>(Map.of(2, "name"))
			);
			assertArrayEquals(new String[]{"name"}, artists.commonArtists(0, 1).get());
		}
	}

	@Test
	public void artistIDToString() {
		assertEquals(0, Artists.artistIDToString(new HashMap<>(), new ArrayList<>()).length);
		{
			assertThrows(RuntimeException.class,
				() -> Artists.artistIDToString(new HashMap<>(), Collections.singletonList(0))
			);
		}
		{
			var map = new HashMap<>(Map.of(0, "a"));
			var result = Artists.artistIDToString(map, Collections.singletonList(0));
			assertArrayEquals(new String[]{"a"}, result);
		}
	}

	@Test
	public void aggregateListenCounts() {
		assertEquals(new HashMap<>(), Artists.aggregateListenCounts(new HashMap<>()));
		{
			var result = Artists.aggregateListenCounts(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1))
			)));
			assertEquals(new HashMap<>(Map.of(1, 1)), result);
		}
		{
			var result = Artists.aggregateListenCounts(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1)),
				1,
				new HashMap<>(Map.of(1, 2))
			)));
			assertEquals(new HashMap<>(Map.of(1, 3)), result);
		}
	}

	@Test
	public void top10ArtistNames() {
		{
			var artists = new Artists(new HashMap<>(), new HashMap<>());
			assertArrayEquals(new String[]{}, artists.top10ArtistNames());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0, new HashMap<>(Map.of(1, 1)))),
				new HashMap<>(Map.of(1, "name"))
			);
			assertArrayEquals(new String[]{"name"}, artists.top10ArtistNames());
		}
		{
			var artists = new Artists(new HashMap<>(Map.of(0,
				new HashMap<>(Map.of(1, 1)),
				1,
				new HashMap<>(Map.of(2, 2))
			)),
				new HashMap<>(Map.of(1, "a", 2, "b"))
			);
			assertArrayEquals(new String[]{
				"b",
				"a"
			}, artists.top10ArtistNames());
		}
	}
}
