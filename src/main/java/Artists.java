import java.util.*;
import java.util.stream.Collectors;

public class Artists {
	public static Artists parseArtists(Readable userArtistsFile, Readable artistsFile) {
		return new Artists(parseUserArtists(userArtistsFile), parseArtistNames(artistsFile));
	}

	static HashMap<Integer, HashMap<Integer, Integer>> parseUserArtists(Readable file) {
		var scanner = new Scanner(file);
		scanner.useDelimiter("\\R");

		// Skip header
		if (scanner.hasNext()) {
			scanner.next();
		}

		var map = new HashMap<Integer, HashMap<Integer, Integer>>();

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
			map.compute(userID, (id, userMap) -> {
				if (userMap == null) {
					return new HashMap<>(Map.of(artistID, listenCount));
				}

				userMap.put(artistID, listenCount);
				return userMap;
			});
		}
		return map;
	}

	static HashMap<Integer, String> parseArtistNames(Readable file) {
		var scanner = new Scanner(file);
		scanner.useDelimiter("\\R");

		// Skip header
		if (scanner.hasNext()) {
			scanner.next();
		}

		var map = new HashMap<Integer, String>();

		while (scanner.hasNext()) {
			var line = scanner.next();
			var tabIndex0 = line.indexOf("\t");
			if (tabIndex0 == -1) {
				throw new ParseException();
			}
			var tabIndex1 = line.indexOf("\t", tabIndex0 + 1);
			if (tabIndex1 == -1) {
				tabIndex1 = line.length();
			}

			var id = Integer.parseInt(line.substring(0, tabIndex0));
			var artistName = line.substring(tabIndex0 + 1, tabIndex1);
			map.put(id, artistName);
		}
		return map;
	}

	// userID -> (artistID -> listenCount)
	private final HashMap<Integer, HashMap<Integer, Integer>> userArtists;
	private final HashMap<Integer, String> artistNames;

	Artists(
		HashMap<Integer, HashMap<Integer, Integer>> userArtists,
		HashMap<Integer, String> artistNames
	) {
		this.userArtists = userArtists;
		this.artistNames = artistNames;
	}

	public Optional<String[]> commonArtists(int user1, int user2) {
		var user1Artists = userArtists.get(user1);
		var user2Artists = userArtists.get(user2);
		if (user1Artists == null || user2Artists == null) {
			return Optional.empty();
		}

		var commonArtists = user1Artists.keySet();
		commonArtists.retainAll(user2Artists.keySet());

		return Optional.of(artistIDToString(artistNames, commonArtists));
	}

	static String[] artistIDToString(HashMap<Integer, String> artistNames, Iterable<Integer> ids) {
		var result = new ArrayList<String>();
		for (var id : ids) {
			var name = artistNames.get(id);
			if (name == null) {
				throw new RuntimeException("name not found: " + id);
			}
			result.add(name);
		}
		return result.toArray(String[]::new);
	}

	static HashMap<Integer, Integer> aggregateListenCounts(
		HashMap<Integer, HashMap<Integer, Integer>> userArtists
	) {
		var artistListenCount = new HashMap<Integer, Integer>();
		for (var user : userArtists.entrySet()) {
			for (var artist : user.getValue().entrySet()) {
				artistListenCount.merge(artist.getKey(), artist.getValue(), Integer::sum);
			}
		}
		return artistListenCount;
	}

	public String[] top10ArtistNames() {
		var listenCounts = new ArrayList<>(aggregateListenCounts(userArtists).entrySet());
		listenCounts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		// Get ids of top 10 artists
		var artistIDs = listenCounts.stream().limit(Integer.min(
			10,
			listenCounts.size()
		)).map(Map.Entry::getKey).collect(Collectors.toList());

		return artistIDToString(artistNames, artistIDs);
	}

	public String[] top10ArtistsOfUsers(ArrayList<Integer> users) {
		throw new UnsupportedOperationException();
	}
}
