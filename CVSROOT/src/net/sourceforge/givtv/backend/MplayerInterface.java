package net.sourceforge.givtv.backend;

import java.io.*;

/**Methods to manage a mplayer window
 * 
 * @author Kyrre Ness Sjøbæk
 *
 */
public class MplayerInterface {
	
	//private final	String	mplayercommand = "mplayer";
	private 		Process	mplayer;
	private			BufferedWriter mplayerCommander;
	
	public void startMplayer (String playfile) {
		System.out.println("Starting mplayer...");
		try {
			//TODO: Wanted: something to stop mplayer from using keyboard controls in the mplayer window
			mplayer = Runtime.getRuntime().exec("mplayer -quiet -slave " + playfile);
			
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
			
			mplayerCommander = new BufferedWriter(new OutputStreamWriter(mplayer.getOutputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void stopMplayer () {
		System.out.println("Stopping mplayer...");
		try {
			mplayerCommander.write("quit\n");
			mplayerCommander.close(); //Will give a backtrace if q has been pressed on mplayer.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mplayer.waitFor();
		
	}
}
