package dperry.aoede.music;

import org.jfugue.elements.Instrument;

public class Voice {

	private String name;
	private VoiceType voiceType;
	private VoiceStyle voiceStyle;
	private int octave;
	
	//TODO: instrument
	
	public Voice() {
		octave = 5;
		voiceType = VoiceType.MELODY;
		voiceStyle = VoiceStyle.PROGRESSION;
		name = "voice";
	}
	
	public Voice( String name, VoiceType voiceType, VoiceStyle voiceStyle, int octave ) {
		this.name = name;
		this.voiceType = voiceType;
		this.voiceStyle = voiceStyle;
		this.octave = octave;
	}
	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	public VoiceType getVoiceType() {
		return voiceType;
	}
	public void setVoiceType( VoiceType voiceType ) {
		this.voiceType = voiceType;
	}
	public VoiceStyle getVoiceStyle() {
		return voiceStyle;
	}
	public void setVoiceStyle( VoiceStyle voiceStyle ) {
		this.voiceStyle = voiceStyle;
	}
	public int getOctave() {
		return octave;
	}
	public void setOctave( int octave ) {
		this.octave = octave;
	}
}
