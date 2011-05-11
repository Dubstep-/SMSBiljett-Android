package com.apps.smsbiljett.tickets;

public class UddevallaTicket extends Ticket {

	public UddevallaTicket() {
		super();
	}

	@Override
	String getPrice() {
		return !mReduced ? "Pris:21,00 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />" : "Pris:15,80 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />";
		}

	@Override
	String getZone() {
		return "UDDEVALLA t�tortzon.<br />";
	}

	@Override
	public int getTicketID() {
		return 7;
	}
	
	@Override
	public String getTitle() {
		return "Uddevalla";
	}

}
