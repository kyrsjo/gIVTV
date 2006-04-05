package org.kyrsjo.givtv.utils;

import org.kyrsjo.givtv.backend.TuneInterface;
import java.util.*;

/**Search for TV channels, using the cards built in signal detection facility
 * 
 * @author Kyrre Ness Sjøbæk
 * 
 * @version 0.9
 */
class SearchChannels {
	final static	String			version		= "0.9";
	
	final private	TuneInterface	tune;
	
					String			freqtable	= null;
	
	/**Set initial required variables
	 * 
	 * @param tunerdevice Which tuner are we searching on? (example "/dev/video0")
	 */
	SearchChannels (String tunerdevice) {
		if (tunerdevice == null) {
			System.err.println("You must specify a tunerdevice!");
			System.exit(0);
		}
		
		tune = new TuneInterface(tunerdevice);	
	}
	
	/**Search all channels in the given frequency map
	 * 
	 * @param freqtable The frequencymap to search trough
	 * @return The names (like "E7" or "11") of all found channels
	 */
	public String[] searchChannels (String freqtable) {
		if (!tune.setFreqtable_current(freqtable)) {
			System.err.println("Couln't set freqtable " + freqtable + " - check spelling");
			System.exit(0);
		}
		String[] searchList = tune.getChannelList()[0];
		ArrayList searchFound = new ArrayList();
		
		System.out.println ("Searching. This may take a moment...");
		
		for (int i = 0; i < searchList.length; i++) {
			//System.out.println ("Tuning to channel " + searchList[i]);
			tune.setChannel_current(searchList[i]);
			if (tune.getSignalDetected()) {
				searchFound.add(searchList[i]);
			}
		}
		
		String[] returnlist = new String[searchFound.size()];
		for (int i = 0; i < searchFound.size(); i++) {
			returnlist[i] = (String) searchFound.get(i);
		}
		
		return returnlist;
		
	}
	
	/**Search all tunable frequencies
	 * 
	 * Note: NOT IMPLEMENTED!
	 * 
	 * @return List of frequencies where signal was detected (like "100.000") (MHz)
	 */
	public String[] searchFrequencies () {
		System.err.println ("Search for frequencies are not yet implemented");
		
		return null;
	}
	
	/**Stand-alone usage
	 * 
	 * @param args Command-line arguments
	 */
	public static void main (String[] args) {
		
		//Parse command line arguments
		String	device				= null;
		String	freqtable			= null;
		boolean list_freqtables		= false;
		boolean search_channels		= false;
		boolean search_frequencies	= false;
		
		//TODO: I am doing args[++i]. What if args[i] was the last in args[]? Unsafe, should be checked
		//TODO: Likewise with the substring commands - they could also be out of range.
		for (int i = 0; i < args.length; i++) {
			//Help
			if (args[i].equals("-h") || args[i].equals("--help")) {
				helpMessage();
			}
			//Set device
			else if (args[i].equals("-d")) {
				device = args[++i];
			}
			/*else if (args[i].substring(0, 9).equals("--device=")) {
				device = args[i].substring(9);
			}*/
			//Set freqtable
			else if (args[i].equals("-t")) {
				freqtable = args[++i];
			}
			/*else if (args[i].substring(0, 12).equals("--freqtable=")) {
				freqtable = args[i].substring(12);
			}*/
			//List freqtables
			else if(args[i].equals("-L") || args[i].equals("--list-freqtable")) {
				list_freqtables = true;
			}
			//Search channels
			else if (args[i].equals("-c") || args[i].equals("--search-channels")) {
				search_channels = true;
			}
			//Search frequencies
			else if (args[i].equals("-f") || args[i].equals("--search-frequencies")) {
				search_frequencies = true;
			}
			//Invalid parameter
			else {
				System.err.println("Invalid parameter: " + args[i]);
				System.err.println();
				helpMessage();
			}
		}
		
		/*
		//Debug:
		System.out.println("Parameters read in:");
		System.out.println("device: " + device);
		System.out.println("freqtable: " + freqtable);
		*/
		
		//Command line error handling
		if (args.length == 0) {
			System.err.println("No parameters given");
			System.err.println();
			helpMessage();
		}
		if (search_channels && search_frequencies) {
			System.err.println ("Can't search both channels and frequencies");
			helpMessage();
		}
		if (search_channels && freqtable == null) {
			System.err.println("Can't search channels without frequencytable");
			helpMessage();
		}
		if (!search_channels && !search_frequencies && !list_freqtables) {
			System.err.println("You must specify what to do. This does nothing!");
			helpMessage();
		}
		//Set up whats needed:
		if (device == null) {
			device = "/dev/video0";
		}
		
		SearchChannels search = new SearchChannels (device);
		
		//Lets get searching!
		
		//List frequencytables and exit
		if(list_freqtables) {
			System.out.println ("Valid frequency maps:");
			String[] freqtables = search.tune.getFreqtable_list();
			for (int i = 0; i < freqtables.length; i++) {
				System.out.println("\t" + freqtables[i]);
			}
			System.exit(0);
		}
		
		//Search for channels
		if (search_channels) {
			System.out.println("Searching trough channel map \"" + freqtable + "\"");
			String[] foundChannels = search.searchChannels(freqtable);
			for (int i = 0; i < foundChannels.length; i++) {
				System.out.println(foundChannels[i]);
			}
		}
		//Search for frequencies
		if (search_frequencies) {
			search.searchFrequencies();
		}
		
	}
	
	/**Print a usage message then exit 
	 *
	 */
	private static void helpMessage() {
		System.out.println("SearchChannels v." + version);
		System.out.println();
		/* Yes, there will be a wrapper script.
		 * Any chance for "official endorsement" so it could be named just
		 * "ivtv-search" or something like that?
		 */
		System.out.println("Usage: givtv-search [OPTIONS]");
		System.out.println();
		System.out.println("-h, --help				 Print this help message and exit");
		System.out.println("-d, --device=DEVICE		 Set video device (default: /dev/video0)");
		System.out.println("-t, --freqtable=STRING   Set frequency map to use (required for -c)");
		System.out.println("-L, --list-freqtable	 List all available frequency mappings");
		System.out.println("-c, --search-channels	 Search trough current frequency map");
		System.out.println("-f, --search-frequencies Search trough every frequency aviable (not implemented)");
		//TODO: Add functionality for capturing a few secounds of every found channel, saving it to given folder
		
		System.exit(0);
	}
}