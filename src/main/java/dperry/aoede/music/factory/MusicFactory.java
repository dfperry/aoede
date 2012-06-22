package dperry.aoede.music.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jfugue.Player;

public class MusicFactory {

	private int tempo;
	private int speed;
	private int signatureBeats;
	private int signatureNote;
	
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
	
	private Map<ChordNum,ChordNum[]> progressions;
	private Map<String,Map<ChordNum, String>> chords;
	
	private Random random;
	
	private void buildChords() {
		chords = new HashMap<String, Map<ChordNum,String>>();
		
		Map<ChordNum, String> keyCmaj = new HashMap<MusicFactory.ChordNum, String>();
		keyCmaj.put( ChordNum.I, "Cmaj" );
		keyCmaj.put( ChordNum.ii, "Dmin" );
		keyCmaj.put( ChordNum.iii, "Emin" );
		keyCmaj.put( ChordNum.IV, "Fmaj" );
		keyCmaj.put( ChordNum.IV, "Gmaj" );
		keyCmaj.put( ChordNum.vi, "Amin" );
		chords.put( "C", keyCmaj );
	}
	
	private void buildProgressions() {
		progressions = new HashMap<ChordNum, ChordNum[]>();
		
		progressions.put(ChordNum.I, new ChordNum[] {ChordNum.I, ChordNum.ii, ChordNum.iii, ChordNum.IV, ChordNum.V, ChordNum.vi});
		progressions.put(ChordNum.ii, new ChordNum[] {ChordNum.iii, ChordNum.V});
		progressions.put(ChordNum.iii, new ChordNum[] {ChordNum.IV, ChordNum.vi});
		progressions.put(ChordNum.IV, new ChordNum[] {ChordNum.I, ChordNum.ii, ChordNum.V});
		progressions.put(ChordNum.V, new ChordNum[] {ChordNum.I, ChordNum.iii, ChordNum.vi});
		progressions.put(ChordNum.vi, new ChordNum[] {ChordNum.ii, ChordNum.IV});
	}
	
	public ChordNum getNextChord( ChordNum last ) {
		
		int index = random.nextInt(progressions.get( last ).length);
		ChordNum[] available = progressions.get( last );
		
		return progressions.get( last )[ random.nextInt(progressions.get( last ).length) ];
	}
	
	private OutputLength outputLength;
	
	public MusicFactory( long seed ) {
		outputLength = OutputLength.LINE;
		random = new Random( seed );
		
		buildChords();
		buildProgressions();
		
	}
	
	public void getNextPiece() {
		
	}
	
	public enum ChordNum {
		I,ii,iii,IV,V,vi
	}
}
