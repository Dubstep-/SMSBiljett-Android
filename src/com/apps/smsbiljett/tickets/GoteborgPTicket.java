package com.apps.smsbiljett.tickets;


import android.text.format.Time;

public class GoteborgPTicket extends Ticket {

	public GoteborgPTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:42,00 kr<br />(ink.6% moms) --VÄSTTRAFIK--<br />" : "Pris:31,50 kr<br />(ink.6% moms) --VÄSTTRAFIK--<br />";
	}

	@Override
	String getZone() {
		return "GÖTEBORG + giltig inom<br />området Göteborg +";
	}
	
	@Override
	protected String getValidTime()
	{
		Time validTime = new Time();
		validTime.set(now.toMillis(false) + 10800000);
		
		String tid = 
			validTime.year + "-" + validTime.month + 1 + "-" + validTime.monthDay + " kl. " + (validTime.hour < 10 ? "0" : "") + validTime.hour + "." + (validTime.minute < 10 ? "0" : "") + validTime.minute + ".";
		return " till<br />" + tid + ". ";
	}

	@Override
	public
	int getTicketID() {
		return 2;
	}
	
	@Override
	public  String getTitle() {
		return "Göteborg+";
	}

}
