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

	public static final String version = "0.1";
	private App givtv					= null;
	private InputSettingsWindow		inputsettingswindow	= new InputSettingsWindow();
	private RecordWindow			recordwindow		= new RecordWindow();
	
	Givtv() {
		createMainWindow();
		createView();
	}
	
	public void show () {
		givtv.showAll();
	}
	
	private void createMainWindow() {
		givtv = new App("gIVTV", "gIVTV tv viewer");
		givtv.setDefaultSize(300, 600);
		givtv.addListener (new LifeCycleListener() {
			public void lifeCycleEvent (LifeCycleEvent event) {	}
			public boolean lifeCycleQuery (LifeCycleEvent event) {
				return shutdown();
			}
		});
	}
	
	private void createView() {
		VBox mainbox = new VBox(false, 0);
		givtv.setContent(mainbox);
		
		Label		channellabel = new Label ("Select channel:");
		mainbox.packStart(channellabel, false, false, 0);
		SimpleList	channellist = new SimpleList ();
		mainbox.packStart(channellist, true, true, 0);
		
		HButtonBox	uppndown	= new HButtonBox();
		Button		uppbutton	= new Button(GtkStockItem.GO_UP);
		Button		downbutton	= new Button(GtkStockItem.GO_DOWN);
		uppndown.add(uppbutton);
		uppndown.add(downbutton);
		mainbox.packStart(uppndown, false, false, 0);		
		
		VButtonBox	buttonrow		= new VButtonBox();
		Button		recordbutton	= new Button (GtkStockItem.MEDIA_RECORD);
		Button		inputsettings	= new Button ("Channel setup");
		Button		encodersettings	= new Button ("Encoder setup");
		Button		closebutton		= new Button (GtkStockItem.CLOSE);
		buttonrow.packStart(recordbutton, true, true, 0);
		buttonrow.packStart(inputsettings, true, true, 0);
		buttonrow.packStart(encodersettings, true, true, 0);
		buttonrow.packStart(closebutton, true, true, 0);
		mainbox.packStart(buttonrow, false, true, 0);
		
		
		recordbutton.addListener(new ButtonListener() {
			public void buttonEvent(ButtonEvent arg0) {
				if (arg0.isOfType(ButtonEvent.Type.CLICK)) {
					recordwindow.show();
				}
			}
		});
		inputsettings.addListener(new ButtonListener() {
			public void buttonEvent(ButtonEvent arg0) {
				if (arg0.isOfType(ButtonEvent.Type.CLICK)) {
					inputsettingswindow.show();
				}
			}
		});
		closebutton.addListener(new ButtonListener() {
			public void buttonEvent(ButtonEvent arg0) {
				if (arg0.isOfType(ButtonEvent.Type.CLICK)) {
					shutdown();
				}
			}
		});
	}
	
	
	private boolean shutdown() {
		Gtk.mainQuit();
		return false;
	}
	
	public static void main(String[] args) {
		Program.initGnomeUI("gIVTV", version, args);
		Givtv givtv = new Givtv();
		givtv.show();
		Gtk.main();
	}
}

