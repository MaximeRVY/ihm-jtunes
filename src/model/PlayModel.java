package model;

import helper.HelpForList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	private List<Map<String,Object>> queue;
	
	Boolean random;
	public PlayModel() {
		player = null;
		currentPlayed = new HashMap<String, Object>();
		state = 0;
		queue = new ArrayList<Map<String,Object>>();
		random = false;
	}
	
	public Map<String, Object> getCurrentPlayed() {
		return currentPlayed;
	}

	public void load(Map<String, Object> file){
		if(!file.isEmpty()){
			if(state != 0)
				stop();
			
			try{
				this.currentPlayed = file;
				player = new LillePlayer((String)file.get("pathname"));
				player.setVolume(this.volume);
				Equalizer eq = new Equalizer();
				eq.getBand(0);
				state = 1;
				setChanged();
				notifyObservers("load");
			}catch (Exception e) {
				e.printStackTrace();
			}
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
			setChanged();
			notifyObservers("play");
		}else if(state == 2){
			player.pause();
			setChanged();
			notifyObservers("pause");
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
		if(player!=null)
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
	
	public void setQueue(List<Map<String, Object>> queue){
		this.queue = queue;
	}
	
	public int getDuration(){
		return player.getDuration();
	}
	
	public void next(){
		if(!currentPlayed.isEmpty()){
			if(!random){
				if(state != 0){
					Integer id = (Integer) currentPlayed.get("id");
					Integer i = HelpForList.instance.indexById(this.queue, id);
					if (i != -1){
						if( i >= (this.queue.size() -1) )
							i = 0;
						else
							i += 1;
						Map<String, Object> nextPlayed = this.queue.get(i);
						this.currentPlayed = nextPlayed;
						this.stop();
						setChanged();
						notifyObservers("change");
						this.PlayPause();
					}
				}
			}
		}
	}
	
	public void previous(){
		if(!currentPlayed.isEmpty()){
			if(!random){
				if(state != 0){
					Integer id = (Integer) currentPlayed.get("id");
					Integer i = HelpForList.instance.indexById(this.queue, id);
					if (i != -1){
						if( i >= (this.queue.size() -1) )
							i = 0;
						else
							i -= 1;
						Map<String, Object> previousPlayed = this.queue.get(i);
						this.currentPlayed = previousPlayed;
						setChanged();
						notifyObservers("change");
						this.stop();
						this.PlayPause();
					}
				}
			}
		}
	}

	
	// Thread for lecture
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
						parent.sendToObservable();
					}
						
					try{
						Thread.sleep(1000);
					}catch(Exception e){ e.printStackTrace(); }
				}
				
				if(player == playerInterne){
					// End Event
					parent.next();
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
