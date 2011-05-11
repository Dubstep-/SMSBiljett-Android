package com.apps.smsbiljett;

import android.text.format.Time;

/**
 * 
 * @author Bob
 * 
 * Represents a single item in the inbox list
 */
public class MessageItem {
	// Useful stuff. Min fantasi = 0
	String[] messages = { "hej :)", "hahahahahahhahahahahahahahahahahaha", "vad gör du?", "k", "kommer du hem ikväll?", "ska du med ut?", "vafan :p", "hemma om 5 min", "Haha true :-) vart är ni?", "Är hos stefan om 10", "Inget", "Okok, inte mkt :p Chillar utomhus osv", "Mmhm", "Det låter bra, kommer sen", "Vaken?", "HAHA e du bakis?" };
	String[] contacts = { "Per", "Random", "Eva-lisa", "Mamma", "Pappa", "Älskling<3", "Arnold", "Chefen :S", "Robin arbete", "Johan B", "perre", "Amanda emo", "Emelie skoog", "vem?", "Emma skolan" };
	
	String mContact;
	String mMessage;
	
	int mSent;
	
	Time mTime;
	
	public MessageItem(Time time)
	{
		mMessage = messages[(int) (Math.random()*messages.length-1)];
		mContact = contacts[(int) (Math.random()*contacts.length-1)];
		
		mSent = (int) (Math.random()*100);
		
		mTime = time;
	}
	
	public MessageItem(Time time, String text, String sender, String date)
	{
		mMessage = text;
		mContact = sender;
		
		mSent = (int) (Math.random()*100);
		
		mTime = time;
	}
	
	public String getContact()
	{
		return mContact;
	}
	
	public String getMessage()
	{
		return mMessage;
	}
	
	public int getSent()
	{
		return mSent;
	}
	
	public Time getTime()
	{
		return mTime;
	}
}
