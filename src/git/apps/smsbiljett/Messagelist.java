package com.apps.smsbiljett;

import java.util.ArrayList;

import com.apps.smsbiljett.R;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author Bob
 * 
 * Base inbox class. 
 */
public class Messagelist extends ListActivity {
	
	private ArrayList<MessageItem> messages = new ArrayList<MessageItem>();
	private OrderAdapter adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.string.title); Why dosen't this work :(
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.messaging);
	        
	        // The generated ticket. As sent from the main activity.
	        Bundle sent = getIntent().getExtras();
	        String text = sent.getString("text").replace("<br />", " ");
	        String sender = sent.getString("sender");
	        String date = sent.getString("date");
	        
	        // Unused
	        Time time = new Time();
	        time.setToNow();
	        
	        // Add generated text to 1st spot in inbox
	        messages.add(new MessageItem(time, text, sender, date));

	        ArrayList<String> usedMessages = new ArrayList<String>();
	        ArrayList<String> usedContacts = new ArrayList<String>();
	        while(messages.size() < 10)
	        {
	        	// Unused
	        	time.set((long) (time.toMillis(true) - Math.random()*100000));
	        	
	        	MessageItem m = new MessageItem(time);
	        	
	        	if(usedMessages.contains(m.getMessage()) || usedContacts.contains(m.getContact()))
	        		continue;
	        	else
	        	{
	        		usedMessages.add(m.getMessage());
	        		usedContacts.add(m.getContact());
	        		messages.add(m);
	        	}
	        }
	        adapter = new OrderAdapter(this, R.layout.rowitem, messages);
	        
	        setListAdapter(adapter);
	}

	private class OrderAdapter extends ArrayAdapter<MessageItem> {

        private ArrayList<MessageItem> items;

        public OrderAdapter(Context context, int textViewResourceId, ArrayList<MessageItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.rowitem, null);
                }
                MessageItem o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                        if (tt != null) {
                              tt.setText(o.getContact()+" (0/"+o.getSent()+")");                            }
                        if(bt != null){
                        	if(o.getMessage().length() > 30)
                        		bt.setText(o.getMessage().substring(0, 24)+"...");
                        	else
                        		bt.setText(o.getMessage());
                        }
                }
                return v;
        }
}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	        if(id < 1 )
	        	finish();
	}

}
