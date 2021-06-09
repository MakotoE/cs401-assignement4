import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class FriendsTest {
	@Test
	public void parseFriends() throws IOException {
		{
			var result = Friends.parseFriends(new StringReader(""));
			assertEquals(0, result.size());
			assertTrue(result.friendsOfUser(2).isEmpty());
		}
		{
			var result = Friends.parseFriends(new StringReader("userID\tfriendID\n2\t275"));
			assertEquals(1, result.size());
			assertEquals(new HashSet<>(Collections.singleton(275)), result.friendsOfUser(2).get());
		}
		{
			var result =
				Friends.parseFriends(Files.newBufferedReader(Path.of("user_friends.dat")));
			assertEquals(1892, result.size());
			var expected = new Integer[]{
				275,
				428,
				515,
				761,
				831,
				909,
				1209,
				1210,
				1230,
				1327,
				1585,
				1625,
				1869,
			};
			assertEquals(new HashSet<>(Arrays.asList(expected)), result.friendsOfUser(2).get());
		}
	}

	@Test
	public void commonFriends() {
		@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
		class Test {
			final Friends friends;
			final int user1;
			final int user2;
			final Optional<HashSet<Integer>> expected;

			Test(Friends friends, int user1, int user2, Optional<HashSet<Integer>> expected) {
				this.friends = friends;
				this.user1 = user1;
				this.user2 = user2;
				this.expected = expected;
			}
		}

		var tests = new Test[]{
			new Test(new Friends(new HashMap<>()), 0, 0, Optional.empty()),
			new Test(new Friends(new HashMap<>(Map.of(0, new HashSet<>(Set.of())))),
				0,
				0,
				Optional.of(new HashSet<>())
			),
			new Test(new Friends(new HashMap<>(Map.of(0, new HashSet<>(Set.of())))),
				0,
				1,
				Optional.empty()
			),
			new Test(new Friends(new HashMap<>(Map.of(
				0,
				new HashSet<>(Set.of(1)),
				1,
				new HashSet<>(Set.of(2))
			))), 0, 1, Optional.of(new HashSet<>())),
			new Test(new Friends(new HashMap<>(Map.of(
				0,
				new HashSet<>(Set.of(2)),
				1,
				new HashSet<>(Set.of(2))
			))), 0, 1, Optional.of(new HashSet<>(Set.of(2)))),
		};

		for (var test : tests) {
			assertEquals(test.expected, test.friends.commonFriends(test.user1, test.user2));
		}
	}
}
