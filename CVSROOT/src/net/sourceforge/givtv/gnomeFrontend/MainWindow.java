package net.sourceforge.givtv.gnomeFrontend;

import net.sourceforge.givtv.backend.*;

import org.gnu.gtk.*;
import org.gnu.gtk.event.*;
import org.gnu.gnome.App;
//import org.gnu.gnome.Program;

class MainWindow {
	private Givtv					mainprog;
	private TuneInterface			tuner;
	
	private App						givtv				= null;
	
	private InputSettingsWindow		inputsettingswindow	= new InputSettingsWindow();
	private RecordWindow			recordwindow		= new RecordWindow();
	private ChannelMap				channelmap			= null;
	
	private TreeView				channellistWidget	= new TreeView ();
	private ListStore				channellist;

	
	MainWindow(ChannelMap channelmap, Givtv givtv, TuneInterface tuner) {
		
		this.mainprog   = givtv;
		this.channelmap = channelmap;
		this.tuner		= tuner;
		
		createMainWindow();
		createView();
		
		populateChannels();	
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
		mainbox.packStart(channellistWidget, true, true, 0);
		
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
		
		//channellist.addListener();
		
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
	
	/**Initiate or update the channellist widget,
	 * and tune to the first channel if there are any channels
	 * 
	 */
	private void populateChannels() {
		
		//Setup the channellist widget
		DataColumnString[] columns = new DataColumnString[1];
		columns[0] = new DataColumnString ();
		channellist = new ListStore(columns);
		channellistWidget.setModel(channellist);
		for (int i = 0; i < channelmap.size(); i++) {
			TreeIter foo = channellist.appendRow();
			System.out.println(channelmap.getChannel(i).getName());
			channellist.setValue (foo, columns[0], channelmap.getChannel(i).getName());
		}
		
		//Tune
		if (channelmap.size() > 0) {
			tuner.setChannel_current(channelmap.getChannel(0).getChannel());
		}
	}
	
	private boolean shutdown() {
		mainprog.shutdown();
		return false;
	}
}
