package sa.com.stc.SimRegister;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;



public class Utils {

	private static String dateformat = "EEE, dd MMM yyyy HH:mm:ss z";
	private static String date = "";

	public static void changeLocale(Context ctx, String lang) {
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		ctx.getResources().updateConfiguration(config,
				ctx.getResources().getDisplayMetrics());
	}

	static Button slideHandleButton;
	static SlidingDrawer slidingDrawer;

	public static String computeSignature(String keyString) {
		try {
			SecretKey secretKey = null;

			byte[] keyBytes = keyString.getBytes();
			secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

			Mac mac = Mac.getInstance("HmacSHA1");

			mac.init(secretKey);

			Locale locale = Locale.ENGLISH;

			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(dateformat,locale);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			String baseString = sdf.format(d);

			date = baseString;

			byte[] text = baseString.getBytes();

			return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
		}
		catch(Exception e) { return null; }
	}

	public static String getDate() {
		return date;
	}

	public static String formatDecimal(String value) {
		DecimalFormat df = new DecimalFormat("0.00");
		if(value!=null && ! value.equals("")) {
			String result = df.format(new Double(value));
			return result;
		}
		return "0.00";
	}




		//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//
		//		builder.setMessage(message)
		//		.setCancelable(false)
		//		.setTitle(title)
		//		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		//
		//			@Override
		//			public void onClick(DialogInterface dialog, int which) {
		//				if(task instanceof SubscribeTask){
		//					SubscribeTask sTask = (SubscribeTask)task;
		//
		//					if(subscribe)sTask.setAction(RequestMethod.POST);
		//					else sTask.setAction(RequestMethod.DELETE); 
		//				}
		//				task.execute();
		//			}
		//		})
		//		.setNegativeButton("No", new DialogInterface.OnClickListener() {
		//
		//			@Override
		//			public void onClick(DialogInterface dialog, int which) {
		//				dialog.cancel();
		//
		//			}
		//		});
		//		AlertDialog alert = builder.create();
		//		alert.show();



}
