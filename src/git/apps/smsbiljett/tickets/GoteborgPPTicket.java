package com.apps.smsbiljett.tickets;

import android.text.format.Time;

public class GoteborgPPTicket extends Ticket {

	public GoteborgPPTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris: 52,10 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />" : "Pris: 39,10 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />";
		}

	@Override
	String getZone() {
		return "G�TEBORG ++ giltig inom omr�det<br />G�teborg ++ ";
	}
	
	@Override
	protected String getValidTime()
	{
		Time validTime = new Time();
		validTime.set(now.toMillis(false) + 10800000);
		
		String tid = 
			validTime.year + "-" + validTime.month + 1 + "-" + validTime.monthDay + " kl. " + (validTime.hour < 10 ? "0" : "") + validTime.hour + "." + (validTime.minute < 10 ? "0" : "") + validTime.minute + ".";
		return "till " + tid + ".<br />";
	}

	@Override
	public
	int getTicketID() {
		return 3;
	}

	@Override
	public String getTitle() {
		return "G�teborg++";
	}

}
