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
		
		Table maintable = new Table(40,2, false);
		inputSettingsWindow.setContent(maintable);
		
		maintable.attach(channellist, 0,2, 0,35); //, AttachOptions.SHRINK, AttachOptions.SHRINK, 0,0);
		
		Button upchannel	= new Button(GtkStockItem.GO_UP);
		Button downchannel	= new Button(GtkStockItem.GO_DOWN);
		maintable.attach (upchannel,		0,1, 35,36, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		maintable.attach (downchannel,		1,2, 35,36, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		
		Button addbutton		= new Button (GtkStockItem.ADD);
		Button searchbutton		= new Button (GtkStockItem.FIND);
		Button propertiesbutton	= new Button (GtkStockItem.PROPERTIES);
		maintable.attach (addbutton,		0,2, 36,37, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		maintable.attach (propertiesbutton, 0,2, 37,38, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		maintable.attach (searchbutton,		0,2, 38,39, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		
		Button closebutton		= new Button (GtkStockItem.CLOSE);
		maintable.attach (closebutton,		0,2, 39,40, AttachOptions.FILL, AttachOptions.SHRINK, 0,0);
		
		
	}
	
	public void show() {
		inputSettingsWindow.showAll();
	}

}
