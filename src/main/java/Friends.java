import java.util.HashMap;
import java.util.Optional;

public class Friends {
	public static Friends parseFriends(Readable file) {
		throw new UnsupportedOperationException();
	}

	private final HashMap<Integer, Integer[]> map;

	public Friends(HashMap<Integer, Integer[]> map) {
		this.map = map;
	}

	public Optional<Integer[]> friendsOfUser(int user) {
		throw new UnsupportedOperationException();
	}

	public Optional<Integer[]> commonFriends(int user1, int user2) {
		throw new UnsupportedOperationException();
	}
}
