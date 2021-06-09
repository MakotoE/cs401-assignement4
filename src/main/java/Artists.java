import java.util.*;

public class Artists {
	public static class ArtistInfo {
		final int artistID;
		final int listenCount;

		ArtistInfo(int artistID, int listenCount) {
			this.artistID = artistID;
			this.listenCount = listenCount;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			ArtistInfo that = (ArtistInfo) o;
			return artistID == that.artistID && listenCount == that.listenCount;
		}

		@Override
		public int hashCode() {
			return Objects.hash(artistID, listenCount);
		}

		@Override
		public String toString() {
			return "ArtistInfo{" + "artistID=" + artistID + ", listenCount=" + listenCount + '}';
		}
	}

	public static Artists parseArtists(Readable userArtistsFile, Readable artistsFile) {
		throw new UnsupportedOperationException();
	}

	public static HashMap<Integer, HashSet<ArtistInfo>> parseUserArtists(Readable file) {
		var scanner = new Scanner(file);
		scanner.useDelimiter("\\R");

		// Skip header
		if (scanner.hasNext()) {
			scanner.next();
		}

		var map = new HashMap<Integer, HashSet<ArtistInfo>>();

		while (scanner.hasNext()) {
			var line = scanner.next();

			var tabIndex0 = line.indexOf("\t");
			var tabIndex1 = line.indexOf("\t", tabIndex0 + 1);
			if (tabIndex0 == -1 || tabIndex1 == -1) {
				throw new ParseException();
			}

			var userID = Integer.parseInt(line.substring(0, tabIndex0));
			var artistID = Integer.parseInt(line.substring(tabIndex0 + 1, tabIndex1));
			var listenCount = Integer.parseInt(line.substring(tabIndex1 + 1));
			map.compute(userID, (id, set) -> {
				var artistInfo = new ArtistInfo(artistID, listenCount);
				if (set == null) {
					return new HashSet<>(Set.of(artistInfo));
				}

				set.add(artistInfo);
				return set;
			});
		}
		return map;
	}

	public static HashMap<Integer, String> parseArtistNames(Readable file) {
		throw new UnsupportedOperationException();
	}

	private final HashMap<Integer, HashSet<ArtistInfo>> userArtists;
	private final HashMap<Integer, String> artistNames;

	public Artists(HashMap<Integer, HashSet<ArtistInfo>> userArtists,
				   HashMap<Integer, String> artistNames) {
		this.userArtists = userArtists;
		this.artistNames = artistNames;
	}

	public Optional<String[]> commonArtists(int user1, int user2) {
		throw new UnsupportedOperationException();
	}

	public String[] top10Artists() {
		throw new UnsupportedOperationException();
	}

	public String[] top10ArtistsOfUsers(ArrayList<Integer> users) {
		throw new UnsupportedOperationException();
	}
}
