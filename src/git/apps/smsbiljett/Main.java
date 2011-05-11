package com.apps.smsbiljett;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.smsbiljett.tickets.AccessTicket;
import com.apps.smsbiljett.tickets.GoteborgPPTicket;
import com.apps.smsbiljett.tickets.GoteborgPTicket;
import com.apps.smsbiljett.tickets.GoteborgTicket;
import com.apps.smsbiljett.tickets.OrebroTicket;
import com.apps.smsbiljett.tickets.StenungsundTicket;
import com.apps.smsbiljett.tickets.Ticket;
import com.apps.smsbiljett.tickets.TrollVanersborgTicket;
import com.apps.smsbiljett.tickets.UddevallaTicket;
import com.apps.smsbiljett.tickets.UppsalaTicket;

public class Main extends Activity {
	
	// Insert link to host the app and info.txt
	// Its vital to preserve formatting 
	// make it null if you dont want to host it
	String host = "http://www.yoursite.com/";
	
	/* format of info.txt
	 * 
	 * version
	 * img_url<->link_url
	 * img_url2<->link_url2
	 * you may have as many img->link pairs as you wish, they are picked by random when displaying
	 * 
	 * Example:
	 * 4
	 * http://zxcvv.webs.com/fragor.jpg<->www.flashback.org/t1047525n
	 * http://zxcvv.webs.com/feedback.jpg<->goo.gl/mod/j61f
     * http://zxcvv.webs.com/stad.jpg<->goo.gl/mod/j61f
     * http://zxcvv.webs.com/host.jpg<->zxcvv.webs.com/
	 */
	
	// Used to cause automatic updates in clients
	public static String VERSION = "4";
	
	private ProgressDialog progressDialog;
	
	boolean mReduced = false;
	String P = "";
	
	String mAdLink;
	
	AccessTicket t;
	
	private Runnable DissmissOverlay = new Runnable() {
  	  public void run() {
  	      // Protect against null-pointer exceptions
  	      if (progressDialog != null) {
  	    	  progressDialog.dismiss();
  	      }
  	      mainMenu();
  	  }
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //INIT TICKETS. Not ideal solution, but it works
        new GoteborgPPTicket();
        new GoteborgPTicket();
        new GoteborgTicket();
        new OrebroTicket();
        new StenungsundTicket();
        new TrollVanersborgTicket();
        new UddevallaTicket();
        new UppsalaTicket();
        
        mainMenu();
    }

    // CREATE "Övriga Sverige"'s MENU
	@Override  
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	TicketManager ticketmgr = TicketManager.get();
    	
    	menu.setHeaderTitle("Välj område"); 
    	for(int i = 4;i<=ticketmgr.getTicketCount();i++)
    	{
			menu.add(0, i, 0, ticketmgr.getTicketFromID(i).getTitle());
    	}
     }

     @Override
     public boolean onContextItemSelected(MenuItem item) {
    	Ticket ticket = null;

    	TicketManager ticketmgr = TicketManager.get();

    	ticket = ticketmgr.getTicketFromID(item.getItemId());
    	ticket.Generate(mReduced);

    	final Handler handler = new Handler();
    	
    	progressDialog = ProgressDialog.show(this, "Genererar...", "Skapar biljett", true,
	            false);
    	
		mainMenu();
		
		final Ticket ti = ticket;
	    Thread t = new Thread() {
	        public void run() {
	        	try {
	        		  
	    	        ContentValues values = new ContentValues();
	    	        values.put("address", ti.getSender());
	    	        values.put("body", Html.fromHtml(ti.getText()).toString());
	    	        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	    	        
	    	        Intent intent = new Intent(Intent.ACTION_MAIN);
	                intent.addCategory(Intent.CATEGORY_LAUNCHER);
	                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	                intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
	                startActivity(intent);
	
	            }
	            catch(Exception e)
	            {
	            }
	            handler.post(DissmissOverlay);
	        }
	    };
	    t.start();

     	return super.onContextItemSelected(item);
	}
    
    public void onAdPressed(View view)
    {
    	Intent linkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mAdLink));
        startActivity(linkIntent);
    }
    
    public void onRecieveBannerInfo(String text)	
	{
        String[] lines = text.split("\n");
        if(lines[0].contains(VERSION) && !lines[1].equals("No info"))
        {
	        int randnum = (int) (Math.random()*(lines.length-1)+1);
	        
	        // Store image and download corresponding link
	        String pickedEntry = lines[randnum];
	        mAdLink = pickedEntry.split("<->")[1];
	        
	        // Download and insert chosen image
	        ImageView imgView =(ImageView)findViewById(R.id.btmImg);
	        Downloader.Image(pickedEntry.split("<->")[0], imgView);
        }
        else if(lines[1].equals("No info") && lines[0].contains(VERSION))
        {
        	ImageView imgView =(ImageView)findViewById(R.id.btmImg);
        	imgView.setVisibility(8);
        	
        }
        else // New patch
        {
        	mAdLink = host + "SMSBiljett.apk";
        	
        	ImageView imgView =(ImageView)findViewById(R.id.btmImg);
        	
        	Resources res = getResources();
        	Drawable drawable = res.getDrawable(R.drawable.old);
	        imgView.setImageDrawable(drawable);
        }
	}
    
    private void goMagic(final int id)
    {
    	String zones = "";
    	
    	CheckBox checkBox = (CheckBox) findViewById(R.id.A);
    	if(checkBox != null)
    	{
	        if (checkBox.isChecked()) {
	            zones += "A";
	        }
	        checkBox = (CheckBox) findViewById(R.id.B);
	        if (checkBox.isChecked()) {
	            zones += "B";
	        }
	        checkBox = (CheckBox) findViewById(R.id.C);
	        if (checkBox.isChecked()) {
	            zones += "C";
	        }
    	}
        final String z = zones;
    	
    	final Handler handler = new Handler();
    	
    	progressDialog = ProgressDialog.show(this, "Genererar...", "Skapar biljett", true,
                false);
        Thread t = new Thread() {
            public void run() {
                createMagic(id, z);
                handler.post(DissmissOverlay);
            }
        };
        t.start();
    }
    
    private void createMagic(int id, String zones)
    {
    	if(id == 0)
    	{
	        if(zones.length() < 1)
	        {
	        	Toast.makeText(getBaseContext(), "Välj minst 1 zon", 1).show();
	        	return;
	        }
	        
	    	// Generate ticket
	        t = new AccessTicket(mReduced, zones);
	        
	        updateLatest("sthlm", zones);
	        
	        try {
	        	ContentValues values = new ContentValues();
			    values.put("address", "72150");
			    values.put("body", mReduced ? "R"+zones : "H"+zones);
			    getContentResolver().insert(Uri.parse("content://sms/sent"), values);
			        
		        values = new ContentValues();
		        values.put("address", t.getSender());
		        values.put("body", Html.fromHtml(t.getText()).toString());
		        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
		        
		        Intent intent = new Intent(Intent.ACTION_MAIN);
	            intent.addCategory(Intent.CATEGORY_LAUNCHER);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	            intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
	            startActivity(intent);
 
	        }
	        catch(Exception e)
	        {
	        	
	        }
    	}
    	else if(id == 2)
    	{
    		Ticket ticket = null;
    		if(P.equals("P"))
    			ticket = TicketManager.get().getTicketFromID(2);
    		else if(P.equals("PP"))
    			ticket = TicketManager.get().getTicketFromID(3);
    		else
    			ticket = TicketManager.get().getTicketFromID(1);
    		ticket.Generate(mReduced);
    		
    		updateLatest("gbg", P);
	        
    		try {
		        ContentValues values = new ContentValues();
		        values.put("address", ticket.getSender());
		        values.put("body", Html.fromHtml(ticket.getText()).toString());
		        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
		        
		        Intent intent = new Intent(Intent.ACTION_MAIN);
	            intent.addCategory(Intent.CATEGORY_LAUNCHER);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	            intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
	            startActivity(intent);
 
	        }
	        catch(Exception e)
	        {
	        	
	        }
    	}
    	
    	// LATEST
    	else if(id == 3)
    	{
    		ContentValues values = null;
    		
    		String[] latest = getLatest();
    		if(latest != null)
    		{
	    		if(latest[1].equals("gbg"))
	    		{
	    			Ticket ticket = null;
	    			
	    			if(latest[3].equals("P"))
	    				ticket = TicketManager.get().getTicketFromID(2);
	    			else if(latest[3].equals("PP"))
	    				ticket = TicketManager.get().getTicketFromID(3);
	    			else // Standard
	    				ticket = TicketManager.get().getTicketFromID(1);
	    			ticket.Generate(latest[2].equals("1"));
	    			
	    			values = new ContentValues();
			        values.put("address", ticket.getSender());
			        values.put("body", Html.fromHtml(ticket.getText()).toString());
			        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	    		}
	    		else if(latest[1].equals("sthlm"))
	    		{
	    			AccessTicket ticket = new AccessTicket(latest[2].equals("1"), latest[3]);
	    			
	    			values = new ContentValues();
			        values.put("address", ticket.getSender());
			        values.put("body", Html.fromHtml(ticket.getText()).toString());
			        getContentResolver().insert(Uri.parse("content://sms/inbox"), values);
	    		}
		        
		        Intent intent = new Intent(Intent.ACTION_MAIN);
	            intent.addCategory(Intent.CATEGORY_LAUNCHER);
	            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
	            intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
	            startActivity(intent);
	 
    		}
    		else 
    		{
	        	mainMenu();
	        }
    	}
    }

    private void mainMenu()
    {
    	setContentView(R.layout.pick);
    	
        // Download it
    	if(host != null)
    	{
	        Downloader.Text(host + "info.txt", this);
    	}
        ImageView imgView =(ImageView)findViewById(R.id.btmImg);
        Resources res = getResources();
    	Drawable drawable = res.getDrawable(R.drawable.connecting);
        imgView.setImageDrawable(drawable);
        
        ImageView btn = (ImageView)findViewById(R.id.rest);
        registerForContextMenu(btn);
    }
    
    public void onGoButton(View view)
    {
    	goMagic(0);
    }
    
    public void onGoButtonRest(View view)
    {
    	goMagic(1);
    }
    
    public void onGoButtonGBG(View view)
    {
    	goMagic(2);
    }
    
    
    public void onPickStockholm(View view)
    {
    	setContentView(R.layout.pick_stockholm);
    }
    
    public void onPickGBG(View view)
    {
    	setContentView(R.layout.pick_gbg);
    }
    public void onPickRest(View view)
    {
    	openContextMenu(view);
    }
    public void onPickLatest(View view)
    {
    	goMagic(3);
    }
    
    public void onGBGSTANDARD(View view)
    {
    	P = "";
    }
    
    public void onGBGP(View view)
    {
    	P = "P";
    }
    public void onGBGPP(View view)
    {
    	P = "PP";
    }
    
    public void setReducedTrue(View view)
    {
    	mReduced = true;
    }
    
    public void setReducedFalse(View view)
    {
    	mReduced = false;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
    		mainMenu();
    		return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private String[] getLatest()
    {
    	SQLiteDatabase DB = this.openOrCreateDatabase("SMSBiljett", MODE_PRIVATE, null);
    	
    	DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                	"Faves" +
                   "(name VARCHAR, area VARCHAR, reduced INT, other VARCHAR);");
    	
    	Cursor c = DB.query("Faves", null, null, null, null, null, null);
    	
    	int nameCol = c.getColumnIndex("name");
    	int baseAreaCol = c.getColumnIndex("area");
    	int reducedCol = c.getColumnIndex("reduced");
    	int otherCol = c.getColumnIndex("other");
    	
    	if(c != null)
    	{    	
			// If this isnt the first time
			if(c.moveToFirst())
			{
				String[] ret = new String[4];
				
				try {
					ret[0] = c.getString(nameCol);
					ret[1] = c.getString(baseAreaCol);
					ret[2] = Integer.toString(c.getInt(reducedCol));
					ret[3] = c.getString(otherCol);
				}
				catch(Exception e)
				{
			        	Toast.makeText(getBaseContext(), "Ett fel i databasen inträffade!", 1).show();
				}
				DB.close();
				return ret;
			}
    	}
    	DB.close();
		return null;
    }
    
    private void updateLatest(String _area, String _other)
    {
    	SQLiteDatabase DB = this.openOrCreateDatabase("SMSBiljett", MODE_PRIVATE, null);
    	
    	DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                	"Faves" +
                   "(name VARCHAR, area VARCHAR, reduced INT, other VARCHAR);");
    	
    	Cursor c = DB.query("Faves", null, null, null, null, null, null);
    	
    	int nameCol = c.getColumnIndex("name");
    	int baseAreaCol = c.getColumnIndex("area");
    	int reducedCol = c.getColumnIndex("reduced");
    	int otherCol = c.getColumnIndex("other");
    	
    	int reduced = mReduced ? 1 : 0;
    	
		// If this isnt the first time
		if(c.moveToFirst())
		{
			DB.execSQL("UPDATE Faves SET area = '"+_area+"' WHERE name = 'latest'");
			DB.execSQL("UPDATE Faves SET reduced = '"+reduced+"' WHERE name = 'latest'");
			DB.execSQL("UPDATE Faves SET other = '"+_other+"' WHERE name = 'latest'");
			
		}
		else
		{
			DB.execSQL("INSERT INTO " +
					"Faves (name, area, reduced, other) " +
					"VALUES ('latest', '"+_area+"', '"+reduced+"','"+_other+"' );");
		}
		DB.close();
    }

	public void onInternetFailed() {
		ImageView imgView = (ImageView)findViewById(R.id.btmImg);
		
		Resources res = getResources();
    	Drawable drawable = res.getDrawable(R.drawable.noconnection);
        imgView.setImageDrawable(drawable);
	}
}