package dperry.aoede.music;

import org.jfugue.Player;

import dperry.aoede.music.factory.MusicFactory;

public class AoedePlayer {
	private Player player;
	private MusicFactory musicFactory;
	
	
	
	public AoedePlayer() {
		player = new Player();
	}
	
	public void setMusicFactory( MusicFactory musicFactory ) {
		this.musicFactory = musicFactory;
	}
	
	public void pause() {
		player.pause();
	}
	
	public void play() {
		if( player.isPaused() ) {
			player.resume();
		}
		else {
			//TODO: spin it up
		}
	}
}
