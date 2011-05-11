package com.apps.smsbiljett.tickets;


public class UppsalaTicket extends Ticket {

	public UppsalaTicket() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	String getPrice() {
		// MAO The user is VUXEN
		return !mReduced ? "Pris: 20 kr (inkl. 6% moms)<br />" : "Pris: 12 kr (inkl. 6% moms)<br />";	                                
	}

	@Override
	String getZone() {
		return "UPPSALA Stadstrafik.<br />";
	}
	
	@Override
	protected String getHeader()
	{
		String relevant = mSender.substring(mSender.length() - 2, mSender.length());
		return  relevant + chars[(int)(Math.random() * 4)] + " " + (mReduced ? "UNGDOM " : "VUXEN ");
	}
	
	// Virtual
	protected void setSender()
	{
		mSender = "72472" + Ticket.stringOfInts(3);
	}

	@Override
	public int getTicketID() {
		return 8;
	}
	
	@Override
	public String getTitle() {
		return "Uppsala";
	}
}
