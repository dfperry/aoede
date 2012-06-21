package dperry.aoede.music;

import org.jfugue.Pattern;
import org.jfugue.Player;
import org.jfugue.Rhythm;
import org.junit.Test;

import dperry.aoede.music.factory.MusicFactory;
import dperry.aoede.music.factory.MusicFactory.ChordSignature;

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
	
	@Test
	public void testPlayInSequence() {
		Player player = new Player();
		player.play( "c d e f g" );
		player.play( "a b c d e" );
	}
	
	@Test
	public void testPlayDrums() {
		Rhythm rhythm = new Rhythm();
		
		rhythm.setLayer(1, "O..oO...O..oOO..");
		rhythm.setLayer(2, "..*...*...*...*.");
		rhythm.setLayer(3, "^^^^^^^^^^^^^^^^");
		rhythm.setLayer(4, "...............!");
		
		rhythm.addSubstitution('O', "[BASS_DRUM]i");
		rhythm.addSubstitution('o', "Rs [BASS_DRUM]s");
		rhythm.addSubstitution('*', "[ACOUSTIC_SNARE]i");
		rhythm.addSubstitution('^', "[PEDAL_HI_HAT]s Rs");
		rhythm.addSubstitution('!', "[CRASH_CYMBAL_1]s Rs");
		rhythm.addSubstitution('.', "Ri");
		
		Pattern pattern = new Pattern( rhythm.getPattern() );
		pattern.repeat(4);
		Player player = new Player();
		player.play(pattern);
	}
	
	@Test
	public void testPlayChords() {
		Player player = new Player();
		player.play( "Ebmaj Bbmaj Gmin Abmaj" );
	}
	
	@Test
	public void testGenerateChordProgression() {
		MusicFactory factory = new MusicFactory( System.currentTimeMillis() );
		ChordSignature current = ChordSignature.I;
		
		System.out.println( "Progression:" );
		for( int i = 0; i < 4; i++ ) {
			current = factory.getNextChord( current );
			System.out.print( current + " " );
		}
	}
}
