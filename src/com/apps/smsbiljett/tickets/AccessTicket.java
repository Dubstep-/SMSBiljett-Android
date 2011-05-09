package com.apps.smsbiljett.tickets;

import android.text.format.Time;

public class AccessTicket {
	
	// Useful stuff
	int[] mPrices = { 30, 45, 60 };
	double mReducedPriceModifier = 0.6;
	int[] mValidTimeSpans = { 75, 75, 120 };

	String mTextBase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static String[] mMonths = { "jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec" };
	
	// Random values used several times in the ticket
	int[] mRandomInts;
	String mRandomString;
	
	// Time of 'purchase' & validity time
	Time mTime;
	Time mValidTime;
	
	// User input goes here
	String mZones; // i.e. "AB"
	boolean mReduced;
	
	// The final values
	String mText;
	String mSender;
	String mControlCode;
	
	
	public AccessTicket(boolean reduced, String zones)
	{
		
		mReduced = reduced;
		mZones = zones;
		
		mRandomInts = new int[4];
		mRandomString = "";
		
		mTime = new Time();
		mTime.setToNow();

		mValidTime = new Time();
		mValidTime.set(mTime.toMillis(false) + mValidTimeSpans[mZones.length()-1] * 60000);
		mTime.setToNow(); // Can't remember if this is necessary
		
		generateRandoms();
		
		generateSenderNumber();
		
		createMessageText();
	}
	
	private void generateRandoms()
	{
		// Used as a char in sender's phone number as well as in the control-code
		mRandomInts[0] = (int) (Math.round(Math.random() * 6) + 3);
		//Used as a char in sender's phone number
		mRandomInts[1] = (int) (Math.round(Math.random() * 9));
		// Used as the digit after the indicated time, in sender's phone number as well as in the control-code
		mRandomInts[2] = (int) (Math.round(Math.random() * 9));
		// Used as a char in sender's phone number as well as in the control-code
		mRandomInts[3] = (int) (Math.round(Math.random() * 9));
		
		for (int i = 0; i < 7; i++) {
			int rnum = (int) Math.floor(Math.random() * (mTextBase.length() - 1));
			mRandomString+= mTextBase.substring(rnum,rnum+1);
		}
	}
	
	private void generateSenderNumber()
	{
		mSender = "72150" + mRandomInts[0] + mRandomInts[1] + mRandomInts[2] + mRandomInts[3];
	}
	
	private void createMessageText()
	{
		// Section of mainly copypasta. Nothing to worry about, really, just grabbing and formatting the time values...
		String monthChar = mTextBase.substring(mTime.month, mTime.month+1);
		String orderDay = Integer.toString(mTime.monthDay);
		if(mTime.monthDay < 10) orderDay = "0"+orderDay;
		
		String orderHour = Integer.toString(mTime.hour);
		if(mTime.hour < 10) orderHour = "0"+orderHour;
		
		String orderMin = Integer.toString(mTime.minute); 
		if(mTime.minute < 10) orderMin = "0"+orderMin;
		
		String validMonth = Integer.toString(mValidTime.month+1);
		if (mValidTime.month+1 < 10) validMonth = "0" + validMonth;
		
		String validDay = Integer.toString(mValidTime.monthDay);
		if (mValidTime.monthDay < 10) validDay = "0" + validDay;
		
		String validHour = Integer.toString(mValidTime.hour);
		if (mValidTime.hour < 10) validHour = "0" + validHour;
		
		String validMin = Integer.toString(mValidTime.minute);
		if (mValidTime.minute < 10) validMin = "0" + validMin;
		
		// As well as the prices
		String pristext;
		String pristext2;
		if (mReduced) {
			pristext = "R";
			pristext2 = "RED PRIS ";
		}
		else {
			pristext = "H";
			pristext2 = "Helt pris ";
		}

		int price = mPrices[mZones.length()-1];
		if (mReduced) price = (int) ((int) price * mReducedPriceModifier);

		// This actually works
		mControlCode = orderHour + orderMin + monthChar + orderDay + mRandomInts[3] + (mRandomInts[0]-3) + mRandomString +  mRandomInts[3];
		
		mText = ""
			+ pristext + "-" + mZones + " " +  orderHour + ":" + orderMin + " " + mRandomInts[2] + mRandomInts[3] + "<br />"
			+ "SL biljett giltig till " + validHour + ":" + validMin + " "
			+ validDay + "-" + validMonth + "-" + mValidTime.year + "<br />"
			+ pristext2 + price + " kr ink 6% moms<br />"
			+ orderHour + orderMin + monthChar + orderDay + mRandomInts[2] + (mRandomInts[0]-3) + mRandomInts[3] + mRandomString + "<br />"
			+ "http://mobil.sl.se";
	}
	
	public String getText()
	{
		return mText;
	}
	
	public String getDate()
	{
		String returnval = mTime.hour + ":"+ mTime.minute;
		if(mTime.minute < 10)
			returnval = mTime.hour + ":0"+ mTime.minute;
		
		// Lengthy one-liner is due to making the first letter upper case
		returnval += ", " + mTime.monthDay + " " + mMonths[mTime.month].substring(0,1).toUpperCase() + mMonths[mTime.month].substring(1);
		return returnval;
	}
	
	public String getSender()
	{
		return mSender;
	}
}
