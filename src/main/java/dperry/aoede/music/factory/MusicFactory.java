package dperry.aoede.music.factory;

import org.jfugue.Player;

public class MusicFactory {

	private int tempo;
	private int speed;
	private int signatureBeats;
	private int signatureNote;
	
	private Player player;
	
	public MusicFactory() {
		player = new Player();
	}
	
	public void getBar() {
		
	}
}
