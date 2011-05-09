package com.apps.smsbiljett.tickets;

public class TrollVanersborgTicket extends Ticket {

	public TrollVanersborgTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:22,70(ink.6%moms)<br />--V�STTRAFIK--<br />" : "Pris:17,00(ink.6%moms)<br />--V�STTRAFIK--<br />";
		}

	@Override
	String getZone() {
		return " ToV giltig p� buss Trollh�ttan/V�nersborgs t�tortzon<br />";
	}

	@Override
	public int getTicketID() {
		return 6;
	}
	
	@Override
	public String getTitle() {
		return "Trollh�ttan/V�nersborg";
	}

}
