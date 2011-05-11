package com.apps.smsbiljett.tickets;

public class GoteborgTicket extends Ticket {

	public GoteborgTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:21,00 kr<br />(ink.6% moms) --VÄSTTRAFIK--<br />" : "Pris:15,00 kr<br />(ink.6% moms) --VÄSTTRAFIK--<br />";
	}

	@Override
	String getZone() {
		return "GÖTEBORG giltig inom Göteborgs kommun ";
	}

	@Override
	public int getTicketID() {
		return 1;
	}
	
	@Override
	public String getTitle() {
		return "Göteborg";
	}

}
