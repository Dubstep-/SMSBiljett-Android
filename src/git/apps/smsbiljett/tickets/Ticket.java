package com.apps.smsbiljett.tickets;

import com.apps.smsbiljett.TicketManager;

import android.text.format.Time;

public abstract class Ticket {
	
	protected String[] chars = {"/", "-", "*", "+"};
	
	protected Time now;
	
	protected int unique;
	
	protected String mID;
	protected String mPrice;
	protected String mTime;
	
	protected boolean mReduced;
	
	protected String mSender;
	protected String mText;
	
	public Ticket()
	{
		TicketManager.get().registerTicket(this);
	}
	
	public void Generate(boolean reduced)
	{
		mReduced = reduced;
		
		now = new Time();
		now.setToNow();
		
		setSender();
		generateText();
	}
	
	abstract String getPrice();
	abstract String getZone();
	abstract public String getTitle();
	
	abstract public int getTicketID();

	private void generateText()
	{
		mText = getHeader();
		mText += getZone();
		mText += getValidTime();
		mText += getPrice();
		mText += getID();
	}
	
	// Virtual
	protected String getValidTime()
	{
		Time validTime = new Time();
		validTime.set(now.toMillis(false) + 5400000);
		
		String tid = 
			validTime.year + "-" + (validTime.month + 1) + "-" + validTime.monthDay + " kl. " + (validTime.hour < 10 ? "0" : "") + validTime.hour + "." + (validTime.minute < 10 ? "0" : "") + validTime.minute + ".";
		return "Giltig till " + tid + "<br />";
	}
	
	// Virtual 
	protected String getHeader()
	{
		String relevant = mSender.substring(mSender.length() - 2, mSender.length());
		return relevant + chars[(int)(Math.random() * 4)] + " " + (mReduced ? "SKOLUNGDOM " : "VUXEN ") + "";
	}
	
	// Virtual
	protected void setSender()
	{
		mSender = "72450" + stringOfInts(3);
	}
	
	private String getID()
	{
		String id = "";
		
		// Random liten bokstav a-z
		id += (char) ((Math.random() * (122-97)) + 97) ;
		id += (char) ((Math.random() * (122-97)) + 97) ;
		id += (char) ((Math.random() * (122-97)) + 97) ;
		
		// Last three numbers
		id += stringOfInts(10).substring(7, 10);
		
		// Today's code, this will not pass an in-depth check
		id += Integer.toString((int)(Math.random() * 9)) + (char) ((Math.random() * 26) + 65);
		
		// Random ID
		id += Integer.toString((int) ((Math.random() * (996871 - 962139)) + 962139));
		
		char monthChar = (char) (now.month + 97);
		return now.hour + "" + now.minute + monthChar + now.monthDay + id;
	}
	
	public static String stringOfInts(int length)
	{
		String ret = "";
		
		for(int i = 0;i < length;i++)
		{
			ret += Integer.toString((int)(Math.random() * 9));
		}
		
		return ret;
	}
	
	public String getText()
	{
		return mText;
	}
	
	public String getSender()
	{
		return mSender;
	}
	
	public String getDate()
	{
		String returnval = now.hour + ":"+ now.minute;
		if(now.minute < 10)
			returnval = now.hour + ":0"+ now.minute;
		
		// Lengthy one-liner is due to making the first letter upper case
		returnval += ", " + now.monthDay + " " + AccessTicket.mMonths[now.month].substring(0,1).toUpperCase() + AccessTicket.mMonths[now.month].substring(1);
		return returnval;
	}
}
