package org.kyrsjo.givtv.gnomeFrontend;

import org.gnu.gtk.*;
import org.gnu.gtk.event.*;
import org.gnu.gnome.App;
import org.gnu.gnome.Program;

/**
 * @author Kyrre Ness Sjøbæk
 *
 */
public class Givtv {
	// Note to self: eclipse run is shift-alt-x, not control-shift-alt-x
	
	public static final String version = "0.1";
	
	public static void main(String[] args) {
		Program.initGnomeUI("gIVTV", version, args);
		MainWindow mainwindow = new MainWindow();
		mainwindow.show();
		Gtk.main();
	}
}

