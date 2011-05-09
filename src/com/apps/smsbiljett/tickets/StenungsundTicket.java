package com.apps.smsbiljett.tickets;

public class StenungsundTicket extends Ticket {

	public StenungsundTicket() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	String getPrice() {
			return !mReduced ? "Pris:21,00 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />" : "Pris:15,80 kr (inkl. 6% moms)<br />--V�STTRAFIK--<br />";
		}

	@Override
	String getZone() {
		return "STENUNGSUND t�tortzon.<br />";
	}

	@Override
	public int getTicketID() {
		return 5;
	}
	
	@Override
	public String getTitle() {
		return "Stenungsund";
	}

}
