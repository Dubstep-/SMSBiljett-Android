package com.apps.smsbiljett.tickets;

public class GoteborgTicket extends Ticket {

	public GoteborgTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:21,00 kr<br />(ink.6% moms) --V�STTRAFIK--<br />" : "Pris:15,00 kr<br />(ink.6% moms) --V�STTRAFIK--<br />";
	}

	@Override
	String getZone() {
		return "G�TEBORG giltig inom G�teborgs kommun ";
	}

	@Override
	public int getTicketID() {
		return 1;
	}
	
	@Override
	public String getTitle() {
		return "G�teborg";
	}

}
