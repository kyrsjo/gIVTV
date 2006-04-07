package org.kyrsjo.givtv.backend;

import java.io.*;
import java.util.*;

//TODO: Add methods/variable for setting the frequency directly
//TODO: Pointers are returned to calling methods, should be "safed" by providing clones instead.

/**A Java wrapper for ivtv-tune
 * 
 * @author Kyrre Ness Sjøbæk
 * @version 0.9
 */
public class TuneInterface {
	
	private final	String		ivtvtune_version;
	
	private			String		freqtable_current;
	private	final	String[]	freqtable_list;
	
	private 		String		channel_current;
	private			String[][]	channel_list = new String [2][];
	
	private			boolean		signal_detected;
	
	private	final	String		videodevice;
	
	
	/**Populates the internal datastructures with default values
	 * 
	 * @param videodevice Which device are we using (usually "/dev/video0")
	 */
	public TuneInterface (String videodevice) {
		//TODO: Check that videodevice actually exists!
		
		this.videodevice = videodevice;
		
		//Check what version we are using
		ivtvtune_version = ivtvtune_run ("--version")[0];

		//Get the list of frequencytables
		//Note: The first line of the output of ivtv-tune --list-freqtable is stderr,
		//so all we have to do is just not capture it
		freqtable_list = ivtvtune_run ("--list-freqtable"); 
		
		//Set a standard freqtable and its channel list
		setFreqtable_current(freqtable_list[0]);
		
		//Tune to a standard frequency
		setChannel_current(channel_list[0][0]);
		
	}
	
	/**Which version if ivtvtune are we using?
	 * 
	 * @return ivtvtune_version
	 */
	public String getIvtvtune_version () {
		return ivtvtune_version;
	}
	
	/**Which frequencytables are aviable?
	 * 
	 * @return A list of selectable frequencytables
	 */
	public String[] getFreqtable_list () {
		return freqtable_list;
	}
	
	/**Switches to a new frequencytable.
	 * 
	 * @param freqtable The new table we want to use - for example "europe-west"
	 * @return true if everything went OK, false if it wasn't able to switch to a new frequencytable
	 */
	public boolean setFreqtable_current (String freqtable) {
		String[][] channel_list = getChannelListFromIvtvtune(freqtable);
		
		if (channel_list != null) {
			this.channel_list = channel_list;
			this.freqtable_current = freqtable;
			return true;
		}
		else {
			return false;
		}
	}
	/**Which frequencytable are we currently using?
	 * 
	 * @return freqtable_current
	 */
	public String getFreqtable_current () {
		return freqtable_current;
	}
	
	/**Tune to another channel
	 * Also checks if there is a signal on the channel (sets signal_detected=true)
	 * 
	 * @param channel The name (more like number, example "SE20") of the channel, as one of the values in channel_list
	 * @return True if everyting went OK, false if it wasn't able to switch to a new frequencytable
	 */
	public boolean setChannel_current (String channel) {
		if (validateChannel(channel)) {
			String returned_value = ivtvtune_run("--freqtable=" + freqtable_current + " --channel=" + channel)[0];
			//System.out.println ("DEBUG: " + returned_value);
			returned_value = returned_value.substring(returned_value.length()-17, returned_value.length());
			
			if (returned_value.equals("(Signal Detected)")) {
				//We have a signal
				signal_detected = true;
			}
			else {
				signal_detected = false;
			}
			
			
			
			return true;
		}
		return false;
	}
	/**Which channel are we currently watching?
	 * 
	 * @return The name (more like number, example "SE20") of the channel, as one of the values in channel_list
	 */
	public String getChannel_current () {
		return channel_current;
	}
	
	/**Returns the current channel list for the current frequencytable
	 * channel_list[0][] is an array containing the channel names (or numbers, like "E7" or "5"), while
	 * channel_list[1][] contains the corresponding frequencies.
	 * 
	 * @return The channel/frequency list for the current frequencytable
	 */
	public String[][] getChannelList () {
		return channel_list;
	}
	
	/**Is there a signal on current channel?
	 * 
	 * @return The value of signal_detected. True if there was a signal when the channel was tuned in, else false.
	 */
	public boolean getSignalDetected () {
		return signal_detected;
	}
	
	/**Asks ivtv-tune for the channel list for a specific frequencytable
	 * 
	 * @param freqtable Which frequencytable do you want?
	 * @return A list of channels, as reported by ivtv, stripped for frequency info (only names), or null if something went wrong
	 */
	private String[][] getChannelListFromIvtvtune (String freqtable) {
		if (validateFreqtable(freqtable)) {
			String[] channel_list = ivtvtune_run("--freqtable=" + freqtable + " --list-channels");
			String[][] returnlist = new String[2][channel_list.length];
			
			//Put frequencies and channelnames in different arrays 
			for (int i = 0; i < channel_list.length; i++) {
				//ivtv-tune outputs in the format "channelnr\tfrequency"
				String [] tempstring = channel_list[i].split("\t"); 
				returnlist[0][i] = tempstring[0];
				returnlist[1][i] = tempstring[1];
				
			}
			
			return returnlist;
		}
		else {
			return null;
		}
	}
	
	/**Validates the given channelname against the current channellist
	 * 
	 * @param channel The channel to validate
	 * @return true if valid, false
	 */
	private boolean validateChannel (String channel) {
		for (int i = 0; i < channel_list[0].length; i++) {
			if (channel_list[0][i].equals(channel)) {
				return true;
			}
		}
		
		System.err.println("Not a valid channel: " + channel);
		
		return false;
	}
	
	/**Validates the given name of a freqtable against the list of valid freqtables
	 * 
	 * @param freqtable The freqtable to validate
	 * @return true if valid, false if not
	 */
	private boolean validateFreqtable (String freqtable) {
		for (int i = 0; i < freqtable_list.length; i++) {
			if (freqtable.equals(freqtable_list[i])) {
				return true;
			}
		}
		
		System.err.println("Not a valid frequencymap: " + freqtable);
		
		return false;
	}
	
	/**Run ivtv-tune with the given arguments.
	 * --device (videodevice) is always given
	 * 
	 * @param args The arguments that you want to be passed to ivtvtune
	 * @return A list of every line of standard output from ivtvtune 
	 */
	private String[] ivtvtune_run (String args) {
		try {
			ArrayList returnlist = new ArrayList();
			
			Process ivtv_tune = Runtime.getRuntime().exec("ivtv-tune --device=" + videodevice + " " + args);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(ivtv_tune.getInputStream()));
			

			ivtv_tune.waitFor();

			//Capture the output
			boolean read_continue = true;
			while (read_continue) {
				String nextline = stdInput.readLine();
				if (nextline != null) {
					returnlist.add(nextline);
				}
				else read_continue = false;
			}
			stdInput.close();
			
			//Process the output for return
			
			String[] returnstring = new String[returnlist.size()];
			for (int i = 0; i < returnlist.size(); i++) {
				returnstring[i] = (String) returnlist.get(i);
			}
			
			return returnstring;
		}
		catch (IOException error) {
			error.printStackTrace();
			System.exit(-1);
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
	
		
	}
}
