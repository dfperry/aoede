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
	
	// major chords: I IV V
	// minor chords: ii iii vi
	
	/*
	 *   Key	I	ii	iii	IV	V	vi
	 *   C		C	Dm	Em	F	G	Am
	 *   Db		Db	Ebm	Fm	Gb	Ab	Bbm
	 *   D		D	Em	F#m	G	A	Bm
	 *   Eb		Eb	Fm	Gm	Ab	Bb	Cm
	 *   E		E	F#m	G#m	A	B	C#m
	 *   F		F	Gm	Am	Bb	C	Dm
	 *   Gb		Gb	Abm	Bbm	Cb	Db	Ebm
	 *   G		G	Am	Bm	C	D	Em
	 *   Ab		Ab	Bbm	Cm	Db	Eb	Fm
	 *   A		A	Bm	C#m	D	E	F#m
	 *   Bb		Bb	Cm	Dm	Eb	F	Gm
	 *   B		B	C#m	D#m	E	F#	G#m
	 * 
	 */
	
	/*
	 * Simple Map
	 * 
	 * 		ii			I   -> ii, iii, IV, V, vi
	 * 		 |\			ii  -> iii, V
	 * 		 |	V		iii -> IV, vi
	 * 		 |/ |		IV  -> I, ii, V
	 * 		iii |		V   -> I, iii, vi
	 * 	     |\ |		vi  -> ii, IV
	 * 		 |	vi		
	 * 		 |/ |
	 * 		IV  |
	 * 	   / |\ |
	 * 	  |  |	ii
	 * 	  |	 |/
	 * 	  |	 V
	 * 	  |	 |
	 * 	   \ |
	 * 		 I
	 * 
	 */
	
	private static String[] progression_I = new String[] {"I", "ii", "iii", "IV", "V", "vi"};
	private static String[] progression_ii = new String[] {"iii", "V"};
	private static String[] progression_iii = new String[] {"IV", "vi"};
	private static String[] progression_IV = new String[] {"I", "ii", "V"};
	private static String[] progression_V = new String[] {"I", "iii", "vi"};
	private static String[] progression_vi = new String[] {"ii", "IV"};
	
	private Map<ChordSignature,ChordSignature[]> progressions;
	
	private Random random;
	
	private void buildProgressions() {
		progressions = new HashMap<ChordSignature, ChordSignature[]>();
		
		progressions.put(ChordSignature.I, new ChordSignature[] {ChordSignature.I, ChordSignature.ii, ChordSignature.iii, ChordSignature.IV, ChordSignature.V, ChordSignature.vi});
		progressions.put(ChordSignature.ii, new ChordSignature[] {ChordSignature.iii, ChordSignature.V});
		progressions.put(ChordSignature.iii, new ChordSignature[] {ChordSignature.IV, ChordSignature.vi});
		progressions.put(ChordSignature.IV, new ChordSignature[] {ChordSignature.I, ChordSignature.ii, ChordSignature.V});
		progressions.put(ChordSignature.V, new ChordSignature[] {ChordSignature.I, ChordSignature.iii, ChordSignature.vi});
		progressions.put(ChordSignature.vi, new ChordSignature[] {ChordSignature.ii, ChordSignature.IV});
	}
	
	public ChordSignature getNextChord( ChordSignature last ) {
		
		int index = random.nextInt(progressions.get( last ).length);
		ChordSignature[] available = progressions.get( last );
		
		return progressions.get( last )[ random.nextInt(progressions.get( last ).length) ];
	}
	
	private OutputLength outputLength;
	
	public MusicFactory( long seed ) {
		outputLength = OutputLength.LINE;
		random = new Random( seed );
		
		buildProgressions();
		
	}
	
	public void getNextPiece() {
		
	}
	
	public enum ChordSignature {
		I,ii,iii,IV,V,vi
	}
}
