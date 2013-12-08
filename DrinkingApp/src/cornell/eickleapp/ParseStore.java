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
import android.widget.Toast;
import android.provider.Settings.Secure;


public class ParseStore extends Activity{
	String mPhoneNumber;
	String android_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DatabaseHandler db=new DatabaseHandler(this);
		List<DatabaseStore> list=db.dataDump();
		android_id= Secure.getString(getBaseContext().getContentResolver(),
                Secure.ANDROID_ID); 
		sendData(list);

		setContentView(R.layout.parse);

	}
	private void sendData(List<DatabaseStore>dataList){
		Parse.initialize(this,  "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw", "8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
		

		  

		
		
		DatabaseStore item;
		for (int i=0;i<dataList.size();i++){
			item=dataList.get(i);
			ParseObject researchObject = new ParseObject("research");
			researchObject.put("variableName", item.variable);
			researchObject.put("varValue", item.value);
			researchObject.put("dateValue", item.date);
			researchObject.put("userId", android_id);
			researchObject.put("groupNo", 2);
			researchObject.saveInBackground();
		}
		

		Toast.makeText(getApplicationContext(), "Your Data Has Been Sent", Toast.LENGTH_SHORT).show();
	}

}
