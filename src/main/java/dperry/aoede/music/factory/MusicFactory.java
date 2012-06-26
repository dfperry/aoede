package dperry.aoede.music.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.jfugue.Pattern;
import org.jfugue.Player;

import dperry.aoede.music.AoedePlayer;
import dperry.aoede.music.Voice;
import dperry.aoede.music.VoiceType;

/**
 * The MusicFactory class builds pieces of music to be played by the enclosing {@link AoedePlayer} class. The music is built by 
 * generating and layering the given vocal layers together in blocks ranging in size from 1 bar to 16 bars.
 * @author dperry
 *
 */
public class MusicFactory {

	private int tempo;
	private int speed;
	private int signatureBeats;
	private int signatureNote;
	
	private static final int OCTAVE_SIZE = 7;
	
	private static String[] notes     = new String[]{ "C",  "D",  "E",  "F",  "G",  "A",  "B"  };
	
	private static String[] ionian     = new String[]{ "C",  "D",  "E",  "F",  "G",  "A",  "B"  };
	private static String[] dorian     = new String[]{ "C",  "D",  "Eb", "F",  "G",  "A",  "Bb" };
	private static String[] phrygian   = new String[]{ "C",  "Db", "Eb", "F",  "G",  "Ab", "Bb" };
	private static String[] lydian     = new String[]{ "C",  "D",  "E",  "F#", "G",  "A",  "B"  };
	private static String[] mixolydian = new String[]{ "C",  "D",  "E",  "F",  "G",  "A",  "Bb" };
	private static String[] aeolian    = new String[]{ "C",  "D",  "Eb", "F",  "G",  "Ab", "Bb" };
	private static String[] locrian    = new String[]{ "C",  "Db", "Eb", "F",  "Gb", "Ab", "Bb" };
	
	private static String[] progression_I = new String[] {"I", "ii", "iii", "IV", "V", "vi"};
	private static String[] progression_ii = new String[] {"iii", "V"};
	private static String[] progression_iii = new String[] {"IV", "vi"};
	private static String[] progression_IV = new String[] {"I", "ii", "V"};
	private static String[] progression_V = new String[] {"I", "iii", "vi"};
	private static String[] progression_vi = new String[] {"ii", "IV"};
	
	private static int[] melodyProgression = new int[]{ -1, -1, -1, -1, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 4, 4, 5 };
	
	public enum ChordNum {
		I(0),ii(1),iii(2),IV(3),V(4),vi(5);
		
		private int index;
		
		private ChordNum( int index ) {
			this.index = index;
		}
		
		public int getIndex() {
			return index;
		}
	}
	
	public class Chord {
		private String key;
		private String chord;
		
		public Chord(String key, String chord) {
			this.key = key;
			this.chord = chord;
		}
		
		public String getKey() {
			return key;
		}
		
		public String getChord() {
			return chord;
		}
	}
	
	public enum Key {
		C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B
	}
	
	private Map<ChordNum,ChordNum[]> progressions;
	private Map<Key,Map<ChordNum, Chord>> keyChords;
	private Key currentKey;
	
	private Random random;
	
	private Map<String, Voice> voices;
	
	private OutputLength outputLength;
	
	/**
	 * Defines the major/minor chords for the standard musical keys
	 */
	private void buildChords() {
		keyChords = new HashMap<Key, Map<ChordNum,Chord>>();
		
		Map<ChordNum, Chord> keyCmaj = new HashMap<MusicFactory.ChordNum, Chord>();
		keyCmaj.put( ChordNum.I, new Chord("C","maj") );
		keyCmaj.put( ChordNum.ii, new Chord("D","min") );
		keyCmaj.put( ChordNum.iii, new Chord("E","min") );
		keyCmaj.put( ChordNum.IV, new Chord("F","maj") );
		keyCmaj.put( ChordNum.V, new Chord("G","maj") );
		keyCmaj.put( ChordNum.vi, new Chord("A","min") );
		keyChords.put( Key.C, keyCmaj );
	}
	
	/**
	 * Builds the list of available chord progressions from the chord progression map in notes.txt. <br/>
	 * Example:<br/>
	 * If the current chord is IV, the available chords to be played next are:
	 *  I, ii, or V 
	 */
	private void buildProgressions() {
		progressions = new HashMap<ChordNum, ChordNum[]>();
		
		progressions.put(ChordNum.I, new ChordNum[] {ChordNum.I, ChordNum.ii, ChordNum.iii, ChordNum.IV, ChordNum.V, ChordNum.vi});
		progressions.put(ChordNum.ii, new ChordNum[] {ChordNum.iii, ChordNum.V});
		progressions.put(ChordNum.iii, new ChordNum[] {ChordNum.IV, ChordNum.vi});
		progressions.put(ChordNum.IV, new ChordNum[] {ChordNum.I, ChordNum.ii, ChordNum.V});
		progressions.put(ChordNum.V, new ChordNum[] {ChordNum.I, ChordNum.iii, ChordNum.vi});
		progressions.put(ChordNum.vi, new ChordNum[] {ChordNum.ii, ChordNum.IV});
	}
	
	/**
	 * Selects the next random chord to be played from the given last chord. 
	 * 
	 * @param last ChordNum that was played last
	 * @return ChordNum to be used next
	 */
	public ChordNum getNextChord( ChordNum last ) {
		
		int index = random.nextInt(progressions.get( last ).length);
		ChordNum[] available = progressions.get( last );
		
		return progressions.get( last )[ random.nextInt(progressions.get( last ).length) ];
	}
	
	/**
	 * Constructor that takes a seed value used for random generation.
	 * @param seed long
	 */
	public MusicFactory( long seed ) {
		outputLength = OutputLength.LINE;
		random = new Random( seed );
		
		voices = new HashMap<String, Voice>();
		signatureBeats = 4;
		signatureNote = 4;
		
		currentKey = Key.C;
		
		buildChords();
		buildProgressions();
		
	}
	
	/**
	 * Gets the next piece of music to be played. This method builds music using current voices and styles to generate 
	 * a piece of music whose length is determined by getOutputLength()
	 * 
	 * @return Pattern
	 */
	public Pattern getNextPiece() {
		
		Pattern pattern = new Pattern();
		
		List<ChordNum> chords = buildChordProgression();
		
		int count = 0;
		for( Voice voice : voices.values() ) {
			
			// V9 is for percussion only
			if( voice.getVoiceType() == VoiceType.PERCUSSION ) {
				pattern.add( buildPatternForVoice( voice, 9, chords ) );
			}
			else {
				pattern.add( buildPatternForVoice( voice, count, chords ) );
				count++;
				// skip V9
				if( count == 9 ) {
					count++;
				}
			}
		}
		
		return pattern;
	}
	
	public List<ChordNum> buildChordProgression() {
		List<ChordNum> chords = new ArrayList<ChordNum>();
		
		ChordNum current = ChordNum.I;
		
		for( int i = 0; i < outputLength.getMeasures(); i++ ) {
			current = getNextChord( current );
			chords.add( current );
		}
		
		return chords;
	}
	
	/**
	 * Builds a pattern for the given voice
	 * 
	 * @param voice Voice
	 * @param voiceId int
	 * @return Pattern
	 */
	private Pattern buildPatternForVoice( Voice voice, int voiceId, List<ChordNum> chords ) {
		switch( voice.getVoiceType() ) {
			case HARMONY:
				return buildHarmonyPattern( voice, voiceId, chords );
			case MELODY:
				return buildMelodyPattern( voice, voiceId, chords );
			case PERCUSSION:
				return buildPercussionPattern( voice, chords );
			case RHYTHM:
				return buildRhythmPattern( voice, voiceId, chords );
		}
		return null;
	}
	
	/**
	 * Generates a harmony pattern for the given voice
	 * 
	 * @param voice Voice
	 * @param voiceId int
	 * @return Pattern
	 */
	private Pattern buildHarmonyPattern( Voice voice, int voiceId, List<ChordNum> chords ) {
		Pattern pattern = new Pattern( "V" + voiceId );
		
		return pattern;
	}
	
	/**
	 * Generates a melody pattern for the given voice
	 * 
	 * @param voice Voice
	 * @param voiceId int
	 * @return Pattern
	 */
	private Pattern buildMelodyPattern( Voice voice, int voiceId, List<ChordNum> chords ) {
		Pattern pattern = new Pattern( "V" + voiceId );
		
		// using the melodyProgression array, build an array of ints
		// then, map those ints to notes in the current scale
		
		
		int[] notes = new int[ chords.size() * signatureBeats ];
		
		int currentNote = random.nextInt( OCTAVE_SIZE );
		
		for( int i = 0; i < notes.length; i++ ) {
			
			// get next change
			int nextChange = melodyProgression[ random.nextInt( melodyProgression.length ) ];
			
			if( nextChange == -1 ) {
				notes[i] = -1;
			}
			else {
				int direction = random.nextBoolean() ? 1 : -1;
				currentNote += ( direction * nextChange );
				notes[i] = currentNote;
			}
		}
		
		System.out.println( "# Melody change progression: " );
		for( int note : notes ) {
			System.out.print( note + " " );
		}
		System.out.println( "\n# " );
		
		
		StringBuilder sb = new StringBuilder();
		int currentOctave = voice.getOctave();
		
		for( int note : notes ) {
			
			if( note == -1 ) {
				sb.append( "rq " );
			}
			else {
				currentOctave = voice.getOctave() + ( note / OCTAVE_SIZE );
				note = ( ( note % (OCTAVE_SIZE) ) + OCTAVE_SIZE ) % OCTAVE_SIZE;
				sb.append( this.notes[note] + "" +  currentOctave + "q ");
			}
		}
		
		System.out.println( "# Melody note progression: " );
		System.out.println( sb.toString() );
		System.out.println( "\n# " );
		
		pattern.add( sb.toString() );
		
		return pattern;
	}
	
	/**
	 * Generates a rhythm pattern for the given voice
	 * 
	 * @param voice Voice
	 * @param voiceId int
	 * @return Pattern
	 */
	private Pattern buildRhythmPattern( Voice voice, int voiceId, List<ChordNum> chords ) {
		Pattern pattern = new Pattern( "V" + voiceId );
		
		System.out.println( "# Rhythm chord progression: " );
		for( ChordNum chord : chords) {
			System.out.print( chord + " " );
		}
		System.out.println( "\n# " );
		
		StringBuilder sb = new StringBuilder();
		
		switch( voice.getVoiceStyle() ) {
			case HOLD:
				
				for( ChordNum chordNum : chords ) {
					Chord chord = keyChords.get( currentKey ).get( chordNum );
					sb.append( chord.getKey() + voice.getOctave() + chord.getChord() + "w " );
				}
				
				pattern.add( sb.toString() );
				
				break;
			case REPEAT:
				
				break;
		}
		
		System.out.println( "# Rhythm note progression: " );
		System.out.println( sb.toString() );
		System.out.println( "\n# " );
		
		return pattern;
	}
	
	/**
	 * Generates a percussion pattern for the given voice
	 * 
	 * @param voice Voice
	 * @return Pattern
	 */
	private Pattern buildPercussionPattern( Voice voice, List<ChordNum> chords ) {
		Pattern pattern = new Pattern( "V9" );
		
		return pattern;
	}
	
	public void addVoice( Voice voice ) {
		voices.put( voice.getName(), voice );
	}
	
	public Map<String, Voice> getVoices() {
		return voices;
	}
	
	public void removeVoice( String name ) {
		voices.remove( name );
	}
	
	public int getTempo() {
		return tempo;
	}

	public void setTempo( int tempo ) {
		this.tempo = tempo;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed( int speed ) {
		this.speed = speed;
	}

	public int getSignatureBeats() {
		return signatureBeats;
	}

	public void setSignatureBeats( int signatureBeats ) {
		this.signatureBeats = signatureBeats;
	}

	public int getSignatureNote() {
		return signatureNote;
	}

	public void setSignatureNote( int signatureNote ) {
		this.signatureNote = signatureNote;
	}

	public OutputLength getOutputLength() {
		return outputLength;
	}

	public void setOutputLength( OutputLength outputLength ) {
		this.outputLength = outputLength;
	}
}
