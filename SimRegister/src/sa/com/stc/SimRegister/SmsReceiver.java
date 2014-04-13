package sa.com.stc.SimRegister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class  SmsReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {

	       Bundle extras = intent.getExtras();
	       if (extras == null)
	       return;
	       String sender = null,body="";
	      // To display a Toast whenever there is an SMS.
	      //Toast.makeText(context,"Recieved",Toast.LENGTH_LONG).show();

	       Object[] pdus = (Object[]) extras.get("pdus");
	       for (int i = 0;  i < pdus.length; i++) {
	          SmsMessage SMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
	          sender = SMessage.getOriginatingAddress();
	          Log.d("sender",sender);
	          body += SMessage.getMessageBody().toString();
	        //  Log.d("body : "+i,body);
	          
	        

	        // This is used to abort the broadcast and can be used to silently
	        // process incoming message and prevent it from further being 
	        // broadcasted. Avoid this, as this is not the way to program an app.
	        // this.abortBroadcast();
	        }
	       if(sender.equalsIgnoreCase("stc"))
	          {
	        	  // A custom Intent that will used as another Broadcast
	 	         Intent in = new Intent("SmsMessage.intent.stc").
	 	         putExtra("get_msg",body);

	 	         //You can place your check conditions here(on the SMS or the sender)            
	 	         //and then send another broadcast 
	 	         context.sendBroadcast(in);
	          }
	     }
	 }
