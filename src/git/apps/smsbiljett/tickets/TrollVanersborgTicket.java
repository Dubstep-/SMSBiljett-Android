package com.apps.smsbiljett.tickets;

public class TrollVanersborgTicket extends Ticket {

	public TrollVanersborgTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:22,70(ink.6%moms)<br />--VÄSTTRAFIK--<br />" : "Pris:17,00(ink.6%moms)<br />--VÄSTTRAFIK--<br />";
		}

	@Override
	String getZone() {
		return " ToV giltig på buss Trollhättan/Vänersborgs tätortzon<br />";
	}

	@Override
	public int getTicketID() {
		return 6;
	}
	
	@Override
	public String getTitle() {
		return "Trollhättan/Vänersborg";
	}

}
