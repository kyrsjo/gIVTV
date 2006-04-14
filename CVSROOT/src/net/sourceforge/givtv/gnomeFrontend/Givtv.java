package net.sourceforge.givtv.gnomeFrontend;

import net.sourceforge.givtv.backend.MplayerInterface;
import net.sourceforge.givtv.backend.TuneInterface;

import org.gnu.gtk.*;
//import org.gnu.gtk.event.*;
//import org.gnu.gnome.App;
import org.gnu.gnome.Program;

/**
 * @author Kyrre Ness Sjøbæk
 *
 */
public class Givtv {
	
	private TuneInterface			tuner;
	private MplayerInterface		mplayer;
	
	static ChannelMap channelmap;
	
	Givtv(String[] args) {
		Program.initGnomeUI("gIVTV", version, args);
		
		//Setup resources
		tuner	= new TuneInterface ("/dev/video0");
		mplayer = new MplayerInterface();
		mplayer.startMplayer("/dev/video0");
		
		//channellist init
		channelmap = new ChannelMap();
		
		//TODO: Read in channels/freqtable from gconf (or something...). This is testing code!
		channelmap.addChannel(new Channel("0", "E9", "NRK1"));
		channelmap.addChannel(new Channel("0", "E8", "NRK2"));
		channelmap.addChannel(new Channel("0", "E10", "Tv2"));
		tuner.setFreqtable_current("europe-west");
		
		//Mainwindow init
		MainWindow mainwindow = new MainWindow(channelmap, this, tuner);
		mainwindow.show();
		
		Gtk.main();
	}
	
	public void shutdown() {
		Gtk.mainQuit();
		mplayer.stopMplayer();
	}
	
	// Note to self: eclipse run is shift-alt-x, not control-shift-alt-x
	
	public static final String version = "0.1";
	
	public static void main(String[] args) {
		Givtv givtv = new Givtv(args);
	}
	
}

