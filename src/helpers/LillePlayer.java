package helpers;

/*
 * 11/19/04		1.0 moved to LGPL.
 * 29/01/00		Initial version. mdm@techie.com
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

import java.io.FileInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.JavaSoundAudioDevice;

/**
 * The <code>LillePlayer</code> class implements a simple player for playback
 * of an MPEG audio stream. 
 * 
 * @author	Mat McGowan
 * @since	0.0.8
 */

// REVIEW: the audio device should not be opened until the
// first MPEG audio frame has been decoded. 
public class LillePlayer
{	  	
	/**
	 * The current frame number. 
	 */
	private int frame = 0;
	
	private String path;
	
	/**
	 * The MPEG audio bitstream. 
	 */
	// javac blank final bug. 
	/*final*/ private Bitstream		bitstream;
	
	/**
	 * The MPEG audio decoder. 
	 */
	/*final*/ private Decoder		decoder; 
	
	/**
	 * The AudioDevice the audio samples are written to. 
	 */
	private AudioDevice	audio;
	
	/**
	 * Has the player been closed?
	 */
	private boolean		closed = false;
	
	/**
	 * Has the player played back all frames from the stream?
	 */
	private boolean		complete = false;
	
	private int			lastPosition = 0;
	
	private boolean		playing = false;
	
	private float		ms_per_frame = 0;
	
	private int			duration = 0;
	
	/**
	 * Creates a new <code>LillePlayer</code> instance. 
	 */
	public LillePlayer(String path) throws JavaLayerException
	{
		this(path, null);	
	}
	
	public LillePlayer(String path, AudioDevice device) throws JavaLayerException
	{
		try{
			this.path = path;
			bitstream = new Bitstream(new FileInputStream(path));		
		}catch(Exception ex){
			throw new JavaLayerException("Exception file not found", ex);
		}
		decoder = new Decoder();
		
		if (device!=null)
		{		
			audio = device;
		}
		else
		{			
			FactoryRegistry r = FactoryRegistry.systemRegistry();
			audio = r.createAudioDevice();
		}
		audio.open(decoder);
		
		try{
			do{
				Header h = bitstream.readFrame();
				if(h==null) break;
				frame++;
				ms_per_frame = h.ms_per_frame();
				duration = (int)(frame * ms_per_frame);
				bitstream.closeFrame();
			}while(true);
			
			//reset file
			bitstream.close();
			bitstream = new Bitstream(new FileInputStream(path));
			frame = 0;
		}
		catch (Exception ex)
		{
			throw new JavaLayerException("Exception initialization", ex);
		}
	}
	
	public void play() throws JavaLayerException
	{
		play(Integer.MAX_VALUE);
	}
	
	/**
	 * Plays a number of MPEG audio frames. 
	 * 
	 * @param frames	The number of frames to play. 
	 * @return	true if the last frame was played, or false if there are
	 *			more frames. 
	 */
	public boolean play(int frames) throws JavaLayerException
	{
		boolean ret = true;
		playing = true;
		
		while (frames-- > 0 && ret)
		{
			if (this.playing )
			{
				ret = decodeFrame();			
			}
			else
			{
				try
				{
					Thread.sleep(100);
				}
				catch (Exception e){}
			}
		}
		
		if (!ret)
		{
			// last frame, ensure all data flushed to the audio device. 
			AudioDevice out = audio;
			if (out!=null)
			{				
				out.flush();
				synchronized (this)
				{
					close();
				}				
			}
		}
		complete = true;
		return ret;
	}
	
	/**
	 * skips over a single frame
	 * @return false	if there are no more frames to decode, true otherwise.
	 */
	protected boolean skipFrame() throws JavaLayerException
	{
		Header h = bitstream.readFrame();
		if (h == null) return false;
		frame++;
		bitstream.closeFrame();
		return true;
	}
	
	/**
	 * Plays a range of MPEG audio frames
	 * @param start	The first frame to play
	 * @param end		The last frame to play
	 * @return true if the last frame was played, or false if there are more frames.
	 */
	public boolean play(final int start, final int end) throws JavaLayerException
	{
		boolean ret = true;
		int offset = start;
		while (offset-- > 0 && ret) ret = skipFrame();
		return play(end - start);
	}
	
	/**
	 * Cloases this player. Any audio currently playing is stopped
	 * immediately. 
	 */
	public synchronized void close()
	{		
		AudioDevice out = audio;
		if (out!=null)
		{ 
			closed = true;
			complete = true;
			audio = null;	
			// this may fail, so ensure object state is set up before
			// calling this method. 
			out.close();
			lastPosition = out.getPosition();
			try
			{
				bitstream.close();
			}
			catch (BitstreamException ex)
			{
			}
		}
		frame = 0;
	}
	
	/**
	 * Returns the completed status of this player.
	 * 
	 * @return	true if all available MPEG audio frames have been
	 *			decoded, or false otherwise. 
	 */
	public synchronized boolean isComplete()
	{
		return complete;	
	}
	
	/**
	 * Pause or resume the player.
	 * 
	 */
	public void pause()
	{
		this.playing = !this.playing;
	}
	
	/**
	 * Return the volume of the current audio
	 * sample being played.
	 */
	public synchronized float getVolume()
	{
		if(audio instanceof JavaSoundAudioDevice){
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
			return jsAudio.getLineGain();
		}
		return 0;
	}
	
	/**
	 * Set the volume of the current audio
	 * sample being played.
	 */
	public synchronized void setVolume(float gain)
	{
		if(audio instanceof JavaSoundAudioDevice){
			JavaSoundAudioDevice jsAudio = (JavaSoundAudioDevice) audio;
			jsAudio.setLineGain(gain);
		}
	}
	
	/**
	 * Retrieves the position in milliseconds of the current audio
	 * sample being played. This method delegates to the <code>
	 * AudioDevice</code> that is used by this player to sound
	 * the decoded audio samples. 
	 */
	public int getPosition()
	{
		return (int)(frame * ms_per_frame);
	}
	
	/**
	 * Set the position in milliseconds of the current audio
	 * sample being played.
	 */
	public synchronized void setPosition(int pos)
	{
		if(audio==null) return;
		
		float computePos = 0;
		
		try{
			if( Math.abs(pos - getPosition())/1000 <= 1 )
				return;
			
			playing = false;
			
			if((pos/1000) < (getPosition()/1000)){
				//reset file
				bitstream.close();
				bitstream = new Bitstream(new FileInputStream(path));
				decoder = new Decoder();
				audio.open(decoder);
				frame = 0;
			}
			
			if(pos < 0 || pos > duration)
				return;
			
			do{
				bitstream.readFrame();
				frame++;
				computePos = frame * ms_per_frame;
				bitstream.closeFrame();
			}while(computePos < pos);
			
			playing = true;
		}		
		catch (Exception ex){}
	}	
	
	/**
	 * Return the duration of title.
	 * 
	 * @return duration in milliseconds.
	 */
	public int getDuration()
	{
		return duration;
	}	
	
	/**
	 * Decodes a single frame.
	 * 
	 * @return true if there are no more frames to decode, false otherwise.
	 */
	protected boolean decodeFrame() throws JavaLayerException
	{		
		try
		{
			AudioDevice out = audio;
			if (out==null)
				return false;
			
			Header h = bitstream.readFrame();	
			frame++;
			
			if (h==null)
				return false;
			
			// sample buffer set when decoder constructed
			SampleBuffer output = (SampleBuffer)decoder.decodeFrame(h, bitstream);
			
			synchronized (this)
			{
				out = audio;
				if (out!=null)
				{					
					out.write(output.getBuffer(), 0, output.getBufferLength());
				}				
			}
			
			bitstream.closeFrame();
		}
		catch (Exception ex)
		{
			System.out.println("Decoding exception: ending...");
			return false;
		}
		/*catch (RuntimeException ex)
		 {
		 throw new JavaLayerException("Exception decoding audio frame", ex);
		 }*/
		return true;
	}
	
	
}
