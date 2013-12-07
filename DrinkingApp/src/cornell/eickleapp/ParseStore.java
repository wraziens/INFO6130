package cornell.eickleapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class ParseStore extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		
		setContentView(R.layout.parse);

	}
	private void sendData(List<DatabaseStore>dataList){
		Parse.initialize(this,  "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw", "8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		
		TelephonyManager tMgr =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		  

		
		
		DatabaseStore item;
		for (int i=0;i<dataList.size();i++){
			item=dataList.get(i);
			ParseObject researchObject = new ParseObject(""+item.variable);
			researchObject.put("varValue", item.value);
			researchObject.put("dateValue", item.date);
			researchObject.put("userId", mPhoneNumber);
			researchObject.saveInBackground();
		}
		

		
	}

}
