import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

public class LastFMRecommender {
	private final Friends friends;
	private final Artists artists;

	public LastFMRecommender() throws IOException {
		this.friends = Friends.parseFriends(Files.newBufferedReader(Path.of("user_friends.dat")));
		this.artists = Artists.parseArtists(
			Files.newBufferedReader(Path.of("user_artists.dat")),
			Files.newBufferedReader(Path.of("artists.dat"))
		);
	}

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

	public void listTop10() {
		for (var name : artists.top10ArtistNames()) {
			System.out.println(name);
		}
	}

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
