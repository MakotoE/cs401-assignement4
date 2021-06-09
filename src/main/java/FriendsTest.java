import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

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
			assertArrayEquals(new Integer[]{275}, result.friendsOfUser(2).get());
		}
		{
			var result = Friends.parseFriends(
				Files.newBufferedReader(Path.of("user_friends.dat"))
			);
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
			assertArrayEquals(expected, result.friendsOfUser(2).get());
		}
	}
}
