package com.example.drinkingapp;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseInitialization extends Application {

	public void onCreate() {
		Parse.initialize(this, "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw",
				"8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");

		ParseUser.enableAutomaticUser();

		ParseACL defaultACL = new ParseACL();

		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
}
