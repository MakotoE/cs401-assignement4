import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.io.CharArrayReader;
import java.util.HashMap;
import java.util.HashSet;

@RunWith(JUnitQuickcheck.class)
public class QuickCheck {
	@Property
	public void friendsParse(char[] data) {
		try {
			Friends.parse(new CharArrayReader(data));
		} catch (ParseException ignored) {}
	}

	@Property
	public void friendsOfUser(HashMap<Integer, HashSet<Integer>> map, int user) {
		new Friends(map).friendsOfUser(user);
	}

	@Property
	public void parseUserArtists(char[] data) {
		try {
			Artists.parseUserArtists(new CharArrayReader(data));
		} catch (ParseException ignored) {}
	}

	@Property
	public void parseArtistNames(char[] data) {
		try {
			Artists.parseArtistNames(new CharArrayReader(data));
		} catch (ParseException ignored) {}
	}
}
