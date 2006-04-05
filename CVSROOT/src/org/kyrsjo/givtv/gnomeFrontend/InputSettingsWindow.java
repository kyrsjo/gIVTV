package org.kyrsjo.givtv.gnomeFrontend;

import org.gnu.gtk.*;
import org.gnu.gtk.event.*;
import org.gnu.gnome.App;

class InputSettingsWindow {
	
	App inputSettingsWindow = null;
	
	InputSettingsWindow() {
		inputSettingsWindow = new App("gIVTV (input settings)", "gIVTV (input settings)");
		inputSettingsWindow.setDefaultSize(300, 600);
		inputSettingsWindow.addListener (new LifeCycleListener() {
			public void lifeCycleEvent (LifeCycleEvent event) {	}
			public boolean lifeCycleQuery (LifeCycleEvent event) {
				//Gtk.mainQuit();
				return false;
			}
		});
	}
	
	public void show() {
		/* TODO: BUG: clicking this a second time (after closing input settings window)
		 * gives a backtrace and no window. Oops.
		 */
		inputSettingsWindow.showAll();
	}

}
