import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A collection of database queries for LastFM data.
 */
public class LastFMRecommender {
	private final Friends friends;
	private final Artists artists;

	public LastFMRecommender() throws IOException {
		this.friends = Friends.parse(Files.newBufferedReader(Path.of("user_friends.dat")));
		this.artists = Artists.parse(
			Files.newBufferedReader(Path.of("user_artists.dat")),
			Files.newBufferedReader(Path.of("artists.dat"))
		);
	}

	/**
	 * Prints the list of friends of the given user.
	 * @param user userID
	 */
	public void listFiends(int user) {
		var result = friends.friendsOfUser(user);
		if (result.isEmpty()) {
			System.out.println("user not found");
			return;
		}

		for (var id : result.get()) {
			System.out.println(id);
		}
	}

	/**
	 * Prints user1’s friends that are common with those of user2.
	 * @param user1 userID of first user
	 * @param user2 userID of second user
	 */
	public void commonFriends(int user1, int user2) {
		var result = friends.commonFriends(user1, user2);
		if (result.isEmpty()) {
			System.out.println("user not found");
			return;
		}

		for (var id : result.get()) {
			System.out.println(id);
		}
	}

	/**
	 * Prints the list of artists listened by both users.
	 * @param user1 userID of first user
	 * @param user2 userID of second user
	 */
	public void listArtists(int user1, int user2) {
		var result = artists.commonArtists(user1, user2);
		if (result.isEmpty()) {
			System.out.println("user not found");
			return;
		}

		for (var name : result.get()) {
			System.out.println(name);
		}
	}

	/**
	 * Prints the top 10 most popular artists listened by all users.
	 */
	public void listTop10() {
		for (var name : artists.top10ArtistNames()) {
			System.out.println(name);
		}
	}

	/**
	 * Prints 10 of the most popular artists listened to by the given user and their friends.
	 * @param user userID
	 */
	public void recommend10(int user) {
		var friendsOfUser = friends.friendsOfUser(user);
		if (friendsOfUser.isEmpty()) {
			System.out.println("user not found");
			return;
		}

		var users = new ArrayList<>(Collections.singletonList(user));
		users.addAll(friendsOfUser.get());
		var result = artists.top10ArtistsOfUsers(users);

		if (result.isEmpty()) {
			System.out.println("user not found");
			return;
		}

		for (var name : result.get()) {
			System.out.println(name);
		}
	}
}
