package net.sourceforge.givtv.gnomeFrontend;

import net.sourceforge.givtv.backend.*;

import org.gnu.gtk.*;
import org.gnu.gtk.event.*;
import org.gnu.gnome.App;
//import org.gnu.gnome.Program;

class MainWindow {
	private Givtv					mainprog;
	//private TuneInterface			tuner;
	
	private App						givtv				= null;
	
	/// Todo: refactor!
	private InputSettingsWindow		inputsettingswindow	= new InputSettingsWindow();
	private RecordWindow			recordwindow		= new RecordWindow();
	
	//private ChannelMap				channelmap			= null;
	
	
	private TreeView				channellistWidget	= new TreeView ();
	private ListStore				channellist;
	private DataColumn []			dc;
	//private DataColumnString		datacolumn0;
	
	MainWindow(ChannelMap channelmap, Givtv givtv, TuneInterface tuner) {
		
		this.mainprog   = givtv;
		//this.channelmap = channelmap;
		//this.tuner		= tuner;
		
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
		setupTreeView ();
		
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
	
	/**Clear and refresh the channellistWidget,
	 * and tune to the first channel if there are any channels.
	 * 
	 */
	private void populateChannels() {
		//Clear all entries
		while(true) {
			TreeIter item = channellist.getIter("0");
			if (item == null) break;
			channellist.removeRow(item);
		}
		
		//Populate with entries
		for (int i = 0; i < mainprog.channelmap.size(); i++) {
			TreeIter foo = channellist.appendRow();
			channellist.setValue (foo, (DataColumnString)dc[0], mainprog.channelmap.getChannel(i).getName());
			channellist.setValue (foo, (DataColumnInt)dc[1], i); //Hidden field which gives us the index in channelmap
		}
		
		//Tune
		changeChannel (mainprog.channelmap.getChannel(0));
	}
	
	private void setupTreeView () {
		//Setup the backend storage
		//DataColumn[] columns = new DataColumnString[1];
		//datacolumn0 = new DataColumnString ();
		dc = new DataColumn[2];
		dc[0] = new DataColumnString();
		dc[1] = new DataColumnInt();
		channellist = new ListStore(dc);
		channellistWidget.setModel(channellist);
		
		//Setup the visual appearance of the treeView
		TreeViewColumn col0 = new TreeViewColumn ();
			CellRendererText render0 = new CellRendererText ();
			col0.packStart(render0, true);
			col0.setTitle("Channel name");
			col0.addAttributeMapping(render0, CellRendererText.Attribute.TEXT, (DataColumnString)dc[0]);
		
		channellistWidget.appendColumn(col0);
		
		//General setup of the widget
		channellistWidget.setEnableSearch(true);
		channellistWidget.setAlternateRowColor(true);
		channellistWidget.setHeadersVisible(false);
		
		//Setup listeners
		channellistWidget.getSelection().addListener(new TreeSelectionListener () {
			public void selectionChangedEvent(TreeSelectionEvent event) {
				//The selection changed
				TreePath[] tp = channellistWidget.getSelection().getSelectedRows();
				if (tp.length == 1) {
					TreeIter item = channellist.getIter(tp[0].toString());
					System.out.println ("Channel change - " + channellist.getValue(item, (DataColumnString)dc[0]));
					changeChannel(mainprog.channelmap.getChannel(channellist.getValue(item, (DataColumnInt) dc[1])));

				}
			}
		});
		channellistWidget.addListener(new TreeViewListener () {
			public void treeViewEvent(TreeViewEvent arg0) {
				//Somebody clicked the list
				
			}
		});
		
	}
	
	private void changeChannel (Channel changeTo) {
		if (changeTo != null) {
			mainprog.tuner.setChannel_current(changeTo.getChannel());
		}
		else {
			System.err.println("Can't change to null channel");
		}
	}
	
	private boolean shutdown() {
		mainprog.shutdown();
		return false;
	}
}
