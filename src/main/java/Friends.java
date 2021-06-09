import java.util.*;

public class Friends {
	public static Friends parseFriends(Readable file) {
		var scanner = new Scanner(file);
		scanner.useDelimiter("\\R");
		var map = new HashMap<Integer, ArrayList<Integer>>();

		// Skip header
		if (scanner.hasNext()) {
			scanner.next();
		}

		while (scanner.hasNext()) {
			var line = scanner.next();
			var tabIndex = line.indexOf("\t");
			var userID = Integer.parseInt(line.substring(0, tabIndex));
			var friendID = Integer.parseInt(line.substring(tabIndex + 1));
			map.compute(userID, (id, list) -> {
				if (list == null) {
					return new ArrayList<>(Collections.singletonList(friendID));
				}

				list.add(friendID);
				return list;
			});
		}
		return new Friends(map);
	}

	private final HashMap<Integer, ArrayList<Integer>> map;

	public Friends(HashMap<Integer, ArrayList<Integer>> map) {
		this.map = map;
	}

	public Optional<Integer[]> friendsOfUser(int user) {
		var value = map.get(user);
		if (value == null) {
			return Optional.empty();
		}

		return Optional.of(value.toArray(Integer[]::new));
	}

	public Optional<Integer[]> commonFriends(int user1, int user2) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return this.map.size();
	}
}
