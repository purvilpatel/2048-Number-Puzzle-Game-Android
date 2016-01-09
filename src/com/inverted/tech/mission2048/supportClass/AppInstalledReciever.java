package com.inverted.tech.mission2048.supportClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppInstalledReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(intent.getAction());
		if (intent.getAction().equals("android.intent.action.PACKAGE_INSTALL")) {
			Uri uri = intent.getData();
			System.out.println("package installed\n" + uri.toString());
		}
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			System.out.println("package PACKAGE_ADDED\n");
		}
		if (intent.getAction().equals("android.intent.action.PACKAGE_CHANGED")) {
			System.out.println("package PACKAGE_CHANGED\n");
		}
	}

}
