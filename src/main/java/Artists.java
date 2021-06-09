import java.util.HashMap;
import java.util.Optional;

public class Artists {
	public static Artists parseArtists(Readable userArtistsFile, Readable artistsFile) {
		throw new UnsupportedOperationException();
	}

	public static HashMap<Integer, Integer[]> parseUserArtists(Readable file) {
		throw new UnsupportedOperationException();
	}

	public static HashMap<Integer, String> parseArtistNames(Readable file) {
		throw new UnsupportedOperationException();
	}

	private final HashMap<Integer, Integer[]> userArtists;
	private final HashMap<Integer, String> artistNames;

	public Artists(HashMap<Integer, Integer[]> userArtists, HashMap<Integer, String> artistNames) {
		this.userArtists = userArtists;
		this.artistNames = artistNames;
	}

	public Optional<String[]> commonArtists(int user1, int user2) {
		throw new UnsupportedOperationException();
	}

	public String[] top10Artists() {
		throw new UnsupportedOperationException();
	}

	public String[] top10ArtistsOfUsers(Integer[] users) {
		throw new UnsupportedOperationException();
	}
}
