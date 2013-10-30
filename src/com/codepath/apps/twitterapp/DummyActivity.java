package com.codepath.apps.twitterapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DummyActivity extends FragmentActivity {
	protected boolean pbShow;
	protected int REFRESH_MENU_ITEM_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_dummy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.dummy, menu);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		if (pbShow)
			addRefreshMenuItem(menu);
		else {
			
		}
	    return true;
	}
	
	public void setPbFlag() {
		pbShow = true;
	}
	
	public void resetPbFlag() {
		pbShow = false;
	}

	protected void addRefreshMenuItem(Menu menu){
	    MenuItem mnu1 = menu.add(0, REFRESH_MENU_ITEM_ID, 0, "Refresh");
	    {
	    	mnu1.setActionView(R.layout.action_refresh);
	    	mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    }
	}
	
	protected void delRefreshMenuItem(Menu menu){
	    MenuItem mnu1 = menu.findItem(REFRESH_MENU_ITEM_ID);
	    if (mnu1!=null)
	    	mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
	}
}
