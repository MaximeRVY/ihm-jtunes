package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import javazoom.jl.decoder.Equalizer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.LillePlayer;

public class PlayModel extends Observable{
	
	private LillePlayer player;
	private Map<String,Object> currentPlayed;
	private int state; //0:stop, 1:load, 2:play
	
	private float volume = 1;
	private int position = 0;
	
	public PlayModel() {
		player = null;
		currentPlayed = new HashMap<String, Object>();
		state = 0;
	}
	
	public void load(Map<String, Object> file){
		if(state != 0)
			stop();
		
		try{
			this.currentPlayed = file;
			player = new LillePlayer((String)file.get("pathname"));
			player.setVolume(0);
			Equalizer eq = new Equalizer();
			eq.getBand(0);
			state = 1;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void PlayPause(){
		if(state == 0){
			load(this.currentPlayed);
			PlayPause();
		}else if(state == 1){
			LaunchListenThread llt = new LaunchListenThread(player,this);
			llt.start();
			state = 2;
		}else if(state == 2){
			player.pause();
		}
	}

	public void stop() {
		if(state == 1 || state == 2){
			player.close();
			state = 0;
		}		
	}
	
	
	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		player.setVolume(volume);
	}
	
	public int getPosition(){
		return position;
	}
	
	public void setPosition(int pos){
		player.setPosition(pos);
		position = pos;
	}
	
	public void sendToObservable(){
		setChanged();
		notifyObservers();
	}


	class LaunchListenThread extends Thread{
		private LillePlayer playerInterne;
		private PlayModel parent;
		public LaunchListenThread(LillePlayer p,PlayModel parent){
			playerInterne = p;
			this.parent = parent;
			
		}
		public void run(){
			try{			
				parent.sendToObservable();
				PlayThread pt = new PlayThread();
				pt.start();
				
				while(!playerInterne.isComplete()){
					position = playerInterne.getPosition();
					if(player == playerInterne){
						System.out.println("PositionEvent");
						parent.sendToObservable();
					}
						
					try{
						Thread.sleep(200);
					}catch(Exception e){ e.printStackTrace(); }
				}
				
				if(player == playerInterne){
					System.out.println("EndEvent");
					parent.sendToObservable();
				}
					
			}catch(Exception e){ e.printStackTrace(); }
		}
		
		class PlayThread extends Thread{
			public void run(){
				try {
					playerInterne.play();
				}catch(JavaLayerException e){ e.printStackTrace(); }
			}
		}
	}
	

}
