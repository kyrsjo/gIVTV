package org.kyrsjo.givtv.test;

import org.kyrsjo.givtv.backend.*;


class TestTuneInterface {
	
	public static void main (String [] args) {
		//Create a TuneInterface:
		TuneInterface tune = new TuneInterface("/dev/video0");
		
		//Print out the fields, to check that they are OK:
		System.out.println("ivtvtune_version:");
		System.out.println("\t" + tune.getIvtvtune_version());
		
		System.out.println("freqtable_list:");
		for (int i = 0; i < tune.getFreqtable_list().length; i++) {
			System.out.println("\t" + tune.getFreqtable_list()[i]);
		}
		
		System.out.println("freqtable_current:");
		System.out.println("\t" + tune.getFreqtable_current());
		
		System.out.println("channel_list:");
		for (int i = 0; i < tune.getChannelList()[0].length; i++) {
			System.out.println("\t" + tune.getChannelList()[0][i] + " -:- " + tune.getChannelList()[1][i]);
		}
		
		//Try to change the frequencytable
		System.out.println("Setting freqtable_current to europe_west:");
		tune.setFreqtable_current("europe-west");
		System.out.println("freqtable_current:");
		System.out.println("\t" + tune.getFreqtable_current());
		System.out.println("channel_list:");
		for (int i = 0; i < tune.getChannelList()[0].length; i++) {
			System.out.println("\t" + tune.getChannelList()[0][i] + " -:- " + tune.getChannelList()[1][i]);
		}
		
		//Try to change the channel
		System.out.println("Setting the channel to E9 (NRK1)");
		tune.setChannel_current("E9");
		System.out.println("Signal detected: " + tune.getSignalDetected());
	}
}