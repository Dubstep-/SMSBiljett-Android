package com.apps.smsbiljett;

import java.util.ArrayList;

import com.apps.smsbiljett.tickets.Ticket;

public class TicketManager {
	private static TicketManager singleton = new TicketManager();
	
	private ArrayList<Ticket> tickets = new ArrayList<Ticket>();
	
	public void registerTicket(Ticket t)
	{
		tickets.add(t);
	}
	
	public Ticket getTicketFromID(int id)
	{
		for(int i = 0;i < tickets.size(); i++)
		{
			if(tickets.get(i).getTicketID() == id)
			{
				return tickets.get(i);
			}
		}
		return null;
	}
	
	public int getTicketCount()
	{
		return tickets.size();
	}
	
	public static TicketManager get()
	{
		return singleton;
	}

}
