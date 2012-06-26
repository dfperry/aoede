package dperry.aoede.music;

import java.util.List;

import org.jfugue.Pattern;
import org.jfugue.Player;
import org.jfugue.Rhythm;
import org.junit.Test;

import dperry.aoede.music.factory.MusicFactory;
import dperry.aoede.music.factory.MusicFactory.ChordNum;
import dperry.aoede.music.factory.OutputLength;

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
		
		factory.setOutputLength( OutputLength.HYPERMEASURE );
		
		List<ChordNum> chords = factory.buildChordProgression();
		
		System.out.println( "Progression:" );
		for( ChordNum chord : chords ) {
			System.out.print( chord + " " );
		}
	}
	
	@Test
	public void testGenerateMelodyProgression() {
		Player player = new Player();
		
		MusicFactory factory = new MusicFactory( System.currentTimeMillis() );
		
		Voice voice = new Voice( "melody", VoiceType.MELODY, VoiceStyle.PROGRESSION, 5 );
		factory.addVoice( voice );
		
		factory.setOutputLength( OutputLength.LINE );
		
		player.play( factory.getNextPiece() );
		
	}
	
	@Test
	public void testGenerateRhythmHoldProgression() {
		Player player = new Player();
		
		MusicFactory factory = new MusicFactory( System.currentTimeMillis() );
		
		Voice voice = new Voice( "rhythm", VoiceType.RHYTHM, VoiceStyle.HOLD, 3 );
		factory.addVoice( voice );
		
		factory.setOutputLength( OutputLength.LINE );
		
		player.play( factory.getNextPiece() );
		
	}
	
	@Test
	public void testGenerateMelodyWithRhythmHoldProgression() {
		Player player = new Player();
		
		MusicFactory factory = new MusicFactory( System.currentTimeMillis() );
		
		Voice rhythm = new Voice( "rhythm", VoiceType.RHYTHM, VoiceStyle.HOLD, 3 );
		factory.addVoice( rhythm );
		
		Voice melody = new Voice( "melody", VoiceType.MELODY, VoiceStyle.PROGRESSION, 5 );
		factory.addVoice( melody );
		
		factory.setOutputLength( OutputLength.LINE );
		
		player.play( factory.getNextPiece() );
		
	}
	
	
}
