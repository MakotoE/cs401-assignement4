import java.util.*;

public class Friends {
	public static Friends parseFriends(Readable file) {
		var scanner = new Scanner(file);
		scanner.useDelimiter("\\R");

		// Skip header
		if (scanner.hasNext()) {
			scanner.next();
		}

		var map = new HashMap<Integer, HashSet<Integer>>();

		while (scanner.hasNext()) {
			var line = scanner.next();
			var tabIndex = line.indexOf("\t");
			if (tabIndex == -1) {
				throw new ParseException();
			}
			var userID = Integer.parseInt(line.substring(0, tabIndex));
			var friendID = Integer.parseInt(line.substring(tabIndex + 1));
			map.compute(userID, (id, set) -> {
				if (set == null) {
					return new HashSet<>(Collections.singletonList(friendID));
				}

				set.add(friendID);
				return set;
			});
		}
		return new Friends(map);
	}

	private final HashMap<Integer, HashSet<Integer>> map;

	Friends(HashMap<Integer, HashSet<Integer>> map) {
		this.map = map;
	}

	public Optional<HashSet<Integer>> friendsOfUser(int user) {
		var value = map.get(user);
		if (value == null) {
			return Optional.empty();
		}

		return Optional.of(new HashSet<>(value));
	}

	public Optional<HashSet<Integer>> commonFriends(int user1, int user2) {
		var user1Friends = friendsOfUser(user1);
		var user2Friends = friendsOfUser(user2);

		if (user1Friends.isEmpty() || user2Friends.isEmpty()) {
			return Optional.empty();
		}

		user1Friends.get().retainAll(user2Friends.get());
		return user1Friends;
	}

	public int size() {
		return this.map.size();
	}
}
