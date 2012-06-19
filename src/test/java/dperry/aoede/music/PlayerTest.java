package dperry.aoede.music;

import org.jfugue.Pattern;
import org.jfugue.Player;
import org.junit.Test;

public class PlayerTest {

	@Test
	public void testPlayScale() {
		Player player = new Player();
		player.play( "c d e f g" );
	}
	
	@Test
	public void testPlayTwoVoices() {
		Player player = new Player();
		
		Pattern voice1 = new Pattern("V0");
		Pattern voice2 = new Pattern("V1");
		
		voice1.add( "c d e f g" );
		voice2.add( "e e e d c" );
		
		Pattern song = new Pattern();
		song.add( voice1 );
		song.add( voice2 );
		player.play( song );
	}
}
