package com.inverted.tech.mission2048.supportClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorAlertDialog extends Dialog {

	public final static int NO_ALERT_ICON = 0;

	public ErrorAlertDialog(Context context) {
		super(context);
	}

	public void showErrorAlertDialog(Context context, int icon, String title,
			String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setCancelable(true);

		if (icon != NO_ALERT_ICON)
			alert.setIcon(icon);

		if (title != null)
			alert.setTitle(title);

		if (message != null)
			alert.setMessage(message);

		alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

}
