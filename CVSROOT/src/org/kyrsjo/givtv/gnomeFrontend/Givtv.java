package org.kyrsjo.givtv.gnomeFrontend;

import org.gnu.gtk.*;
//import org.gnu.gtk.event.*;
//import org.gnu.gnome.App;
import org.gnu.gnome.Program;

/**
 * @author Kyrre Ness Sjøbæk
 *
 */
public class Givtv {
	// Note to self: eclipse run is shift-alt-x, not control-shift-alt-x
	
	public static final String version = "0.1";
	private static ChannelMap channelmap;
	
	public static void main(String[] args) {
		Program.initGnomeUI("gIVTV", version, args);
		
		//channellist init
		channelmap = new ChannelMap();
		//TODO: Read in channels from gconf (or something...). This is testing code!
		channelmap.addChannel(new Channel("0", "E9", "NRK1"));
		channelmap.addChannel(new Channel("0", "E8", "NRK2"));
		channelmap.addChannel(new Channel("0", "E10", "Tv2"));
		
		//Mainwindow init
		MainWindow mainwindow = new MainWindow(channelmap);
		mainwindow.show();
		
		Gtk.main();
	}
	
}

