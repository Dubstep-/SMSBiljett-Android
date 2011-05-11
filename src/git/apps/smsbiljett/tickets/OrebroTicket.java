package com.apps.smsbiljett.tickets;


import android.text.format.Time;

public class OrebroTicket extends Ticket {

	public OrebroTicket() {
		super();
	}

	@Override
	protected String getHeader() {
		String relevant = mSender.substring(mSender.length() - 3, mSender.length());
		return relevant + chars[(int)Math.random() * 4] + " " + (mReduced ? "SKOLUNGDOM" : "VUXEN ") + " ÖREBRO<br />";
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris: 20 kr (inkl. 6% moms)<br />" : "Pris:10 kr (inkl. 6% moms)<br />";
	}

	@Override
	String getZone() {
		return "Stadstrafik. ";
	}

	@Override
	public int getTicketID() {
		return 4;
	}
	
	@Override
	public String getTitle() {
		return "Örebro";
	}
}
