/**
 * 
 */
package net.sourceforge.givtv.gnomeFrontend;

import java.util.ArrayList;

/**Holder class for channel lists 
 * 
 * @author Kyrre Ness Sjøbæk
 */
class ChannelMap {
	
	//Data is stored here
	private ArrayList channels = new ArrayList();
	
	/**Add a channel to the last position in the array
	 * 
	 * @param channel The channel to add (use new Channel (...))
	 */
	public void addChannel (Channel channel) {
		channels.add(channel);
	}
	
	/**Get a channel
	 * Since a pointer is returned, you may make changes to it.
	 * 
	 * @param n The channel you want (array number - 0 <= n < size() )
	 * @return The requested channel if n is a valid channelnumber, null if not.
	 */
	public Channel getChannel (int n) {
		if (n < channels.size()) {
			return (Channel) channels.get(n);
		}
		else return null;
	}
	
	/**Delete a channel
	 * 
	 * @param n The channel to delete
	 * @return The deleted channel if success, null if the channelnumber was invalid
	 */
	public Channel killChannel (int n) {
		if (n < channels.size()) {
			return (Channel) channels.remove(n);
		}
		else return null;
	}
	
	/**The size() of the arraylist that holds the channels,
	 * that is, the number of channels
	 * @return The number of channels (first number that isn't a valid channel)
	 */
	public int size() {
		return channels.size();
	}
	
	/**Writes out the channellist to the standard output
	 * Most usefull for debugging purposes
	 */
	public void printMap () {
		System.out.println("channels.size(): " + channels.size());
		for (int i = 0; i < channels.size(); i++) {
			System.out.println("channels[" + i + "] = " + channels.get(i));
		}
	}
}

/**A single channel, holder for channel data
 * 
 * @author Kyrre Ness Sjøbæk
 *
 */
class Channel {
	private String input; 
	private String channel;
	private String name;
	
	/**Create a new channel. Channels are not immutable.
	 * 
	 * @param input		Ivtv card input number
	 * @param channel	Channel, such as "SE9"
	 * @param name		User-defined name of the channel, such as "NRK1"
	 */
	Channel (String input, String channel, String name) {
		
		this.channel = channel; 
		this.name	 = name;
		this.input	 = input;
	}
	
	public String getInput ()					{return input;			}
	public void   setInput (String input)		{this.input = input;	}
	
	public String getChannel ()					{return channel;		}
	public void   setChannel (String channel)	{this.channel = channel;}
	
	public String getName () 					{return name;			}
	public void   setName (String name)			{this.name = name;		}
	
	public String toString() {
		return (name + " " + channel + " " + input);
	}
}