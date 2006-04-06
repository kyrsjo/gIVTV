package org.kyrsjo.givtv.gnomeFrontend;

import org.gnu.gtk.*;
import org.gnu.gtk.event.*;
import org.gnu.gnome.App;

class InputSettingsWindow {
	
	App inputSettingsWindow = null;
	
	SimpleList channellist = new SimpleList();
	
	InputSettingsWindow() {
		inputSettingsWindow = new App("gIVTV (input settings)", "gIVTV (input settings)");
		inputSettingsWindow.setDefaultSize(300, 600);
		inputSettingsWindow.addListener (new LifeCycleListener() {
			public void lifeCycleEvent (LifeCycleEvent event) {	}
			public boolean lifeCycleQuery (LifeCycleEvent event) {
				if(event.isOfType(LifeCycleEvent.Type.DELETE)) {
					inputSettingsWindow.hide();
					return true;
				}
				return false;
			}
		});
		/*VBox mainbox = new VBox(false,0);
		inputSettingsWindow.setContent(mainbox);
		
		mainbox.packStart(channellist);
		
		HButtonBox uppndown = new HButtonBox();
		Button upchannel = new Button(GtkStockItem.GO_UP);
		Button downchannel = new Button(GtkStockItem.GO_DOWN);
		uppndown.packStart(upchannel);
		uppndown.packStart(downchannel);
		mainbox.packStart(uppndown);
		*/
		Table maintable = new Table(5,1,true);
		inputSettingsWindow.setContent(maintable);
		
		maintable.attach(channellist,0,1,0,3);
		
		HButtonBox uppndown = new HButtonBox();
		Button upchannel = new Button(GtkStockItem.GO_UP);
		Button downchannel = new Button(GtkStockItem.GO_DOWN);
		uppndown.packStart(upchannel);
		uppndown.packStart(downchannel);
		maintable.attach(uppndown, 0, 1, 3, 4);
	}
	
	public void show() {
		inputSettingsWindow.showAll();
	}

}
