package org.kyrsjo.givtv.backend;

import java.io.*;

/**Methods to manage a mplayer window
 * 
 * @author Kyrre Ness Sjøbæk
 *
 */
public class MplayerInterface {
	
	private final	String	mplayercommand = "mplayer";
	private 		Process	mplayer;
	
	public void startMplayer (String playfile) {
		System.out.println("Starting mplayer...");
		try {
			mplayer = Runtime.getRuntime().exec("mplayer -quiet " + playfile);
			/*
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(mplayer.getInputStream()));
			//Capture the output
			boolean read_continue = true;
			while (read_continue) {
				String nextline = stdInput.readLine();
				if (nextline != null) {
					System.out.println(nextline);
				}
				else read_continue = false;
			} 
			stdInput.close();
			*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void stopMplayer () {
		mplayer.destroy();
	}
}
