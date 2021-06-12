import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains data about user listening activity and artist names.
 */
public class Artists {
	public static Artists parse(Readable userArtistsFile, Readable artistsFile) {
		return new Artists(parseUserArtists(userArtistsFile), parseArtistNames(artistsFile));
	}

	/**
	 * @param file user_artists.dat file
	 * @return Mapping of userID to a map of artistID to listen count
	 */
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

	/**
	 * @param file artists.dat file
	 * @return Map of artist ID to name
	 */
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

	/**
	 * @return Artists listened to by both user1 and user2.
	 */
	public Optional<List<String>> commonArtists(int user1, int user2) {
		var user1Artists = userArtists.get(user1);
		var user2Artists = userArtists.get(user2);
		if (user1Artists == null || user2Artists == null) {
			return Optional.empty();
		}

		var commonArtists = user1Artists.keySet();
		commonArtists.retainAll(user2Artists.keySet());

		return Optional.of(artistIDToString(artistNames, new ArrayList<>(commonArtists)));
	}

	/**
	 * Maps artist IDs to artist names.
	 */
	static List<String> artistIDToString(HashMap<Integer, String> artistNames, List<Integer> ids) {
		return ids.stream().map(id -> {
			var name = artistNames.get(id);
			if (name == null) {
				throw new RuntimeException("name not found: " + id);
			}
			return name;
		}).collect(Collectors.toList());
	}

	/**
	 * @return Top 10 most popular artists
	 */
	public List<String> top10ArtistNames() {
		// listenCountsOfUsers() only returns empty if user was not found
		//noinspection OptionalGetWithoutIsPresent
		return artistIDToString(artistNames,
			top10Artists(listenCountsOfUsers(userArtists, userArtists.keySet()).get())
		);
	}

	/**
	 * @return Top 10 most popular artists
	 */
	List<Integer> top10Artists(HashMap<Integer, Integer> artistToListenCount) {
		var listenCounts = new ArrayList<>(artistToListenCount.entrySet());
		listenCounts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		return listenCounts.stream().limit(Integer.min(10,
			listenCounts.size()
		)).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	/**
	 * Aggregates listen counts of artists by given users.
	 */
	static Optional<HashMap<Integer, Integer>> listenCountsOfUsers(
		HashMap<Integer, HashMap<Integer, Integer>> userArtists, Iterable<Integer> users
	) {
		var artistListenCount = new HashMap<Integer, Integer>();
		for (var user : users) {
			var artistsOfUser = userArtists.get(user);
			if (artistsOfUser == null) {
				return Optional.empty();
			}

			for (var artist : artistsOfUser.entrySet()) {
				artistListenCount.merge(artist.getKey(), artist.getValue(), Integer::sum);
			}
		}
		return Optional.of(artistListenCount);
	}

	/**
	 * @return Top 10 artists of given users
	 */
	public Optional<List<String>> top10ArtistsOfUsers(ArrayList<Integer> users) {
		var result = listenCountsOfUsers(userArtists, users);
		if (result.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(artistIDToString(artistNames, top10Artists(result.get())));
	}
}
