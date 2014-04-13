package sa.com.stc.SimRegister;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.stc.sim.utils.NetworkingException;
import com.stc.sim.utils.ServiceNotAvailableException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class ZiyarahMainActivity extends Activity {
	static final int DIALOG_SENDING_REQ_STATUS = 0;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
	private Uri idPhotoUri;
	boolean idPhotoHasBeenTaken;
	String MSISDN;
	String destMSISDN;
	String simNumber;
	String iDNumber;
	String idType;
	String countryName;
	String custmerTypeNmae;
	
	String customerFName;
	String customerLName;
	String country;
	int sendingProgress=0;
	static final int SENDINGPROG_START=0;
	static final int SENDINGPROG_EMAIL_SUBMITTED=1;
	static final int SENDINGPROG_SMS_SUBMITTED=2;
	File f ;
	String modeStr = "";
	private ProgressDialog dialog = null ;
	int selectPosition = 0, cutomerTypePos = 0;
	Spinner sp;
	String errorMessage = "Some problem occured please try again later";
	
	private boolean networkError = false;
	private boolean internalError = false;

    String Language;
	
	ArrayList<String> countryNameArray = new ArrayList<String>();
	ArrayList<String> countryNames = new ArrayList<String>();
	ArrayList<String> cRMID = new ArrayList<String>();
	private BroadcastReceiver mIntentReceiver;
	
//	@Override
//	public void onResume() 
//	{
//		super.onResume();
//
//		IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.stc");
//		mIntentReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//		String msg = intent.getStringExtra("get_msg");
//		Log.d("msgFull",msg);
//		//Process the sms format and extract body &amp; phoneNumber
//		msg = msg.replace("\n", "");
//		/*String body = msg.substring(msg.lastIndexOf(":")+1, msg.length());
//		String pNumber = msg.substring(0,msg.lastIndexOf(":"));*/
//		
//		Log.d("msg",msg.substring(msg.length()-6));
//		//TextView mobiletv = (TextView) findViewById(R.id.otploginnumber);
//			
//				}
//		  };
//		this.registerReceiver(mIntentReceiver, intentFilter);
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//sendRequestViaSMS();
        Language = getIntent().getExtras().getString("Language");
        if(Language.equals("ARABIC"))
            setContentView(R.layout.activity_ziyarah_main_ra);
        else
		    setContentView(R.layout.activity_ziyarah_main);


		if (!isNetworkAvailable()){
			onCreateDialog(9);
		}
		
		Button btn = (Button)findViewById(R.id.topbarButton);

		
		Button btn2 = (Button) findViewById(R.id.btnSendRequest);
		btn2.setVisibility(View.VISIBLE);

        if(Language.equals("ARABIC"))
        {
            btn.setText("سوا زيارة");
            btn2.setText("إرسال");
        }
        else
        {
            btn.setText("SAWA Ziyarah");
            btn2.setText("Send");
        }
		
		EditText agentId = (EditText)findViewById(R.id.etMSISDN);
		//TelephonyManager telMgr = (TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
		agentId.setText(getMyPhoneNO());
		agentId.setVisibility(View.GONE);
		
		//agentId.setFocusable(false);
		
		
		
		
		sp = (Spinner) findViewById(R.id.mySpinner);
		sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long id) {
				countryName = parent.getItemAtPosition(pos).toString();
				selectPosition = pos;
				//System.out.println("POSITION:="+ selectPosition);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		LoadingTaskForContent contentTask = new LoadingTaskForContent();
		contentTask.execute();
		
		
		
	/////////////
		
		
		Spinner sp1 = (Spinner) findViewById(R.id.mySpinnerType);
		sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int pos, long id) {
				custmerTypeNmae = parent.getItemAtPosition(pos).toString();
				cutomerTypePos = pos;
			//	System.out.println("POSITION:="+ cutomerTypePos);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		// Get a reference to the AutoCompleteTextView in the layout
	//	AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
		// Get the string array
		String[] cutomerTypes = getResources().getStringArray(R.array.customer_types_array);
		// Create the adapter and set it to the AutoCompleteTextView 
		ArrayAdapter<String> adapter1 = 
		        new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cutomerTypes);
		
		sp1.setAdapter(adapter1);
	}
	
	
	void sendRequestViaSMS(){
		String messagelBody = "";
		
		messagelBody="2222";
		showToast(messagelBody);

		//sendSMS("0554201070",messagelBody);
		sendSMS("902",messagelBody);
	}
	
	private void sendSMS(String phoneNumber, String message)
	{        
		Log.v("phoneNumber",phoneNumber);
		Log.v("MEssage",message);
		PendingIntent pi = PendingIntent.getActivity(this, 0,
				new Intent(this, Object.class), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, pi, null);        
	}    

	
	private String getMyPhoneNO(){  
	    TelephonyManager mTelephonyMgr;    
	    TelephonyManager tMgr =(TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	    String mPhoneNumber = tMgr.getSimSerialNumber();
	    
	    
	    if(mPhoneNumber != null && !mPhoneNumber.equals("") && mPhoneNumber.length() > 18)mPhoneNumber = mPhoneNumber.substring(0, mPhoneNumber.length()-1);
	    
//	    if(mPhoneNumber != null && !mPhoneNumber.trim().equals(""))
//	    {
//	    	if(mPhoneNumber.contains("+966") || mPhoneNumber.contains("00966"))
//	    	mPhoneNumber = tMgr.getLine1Number().substring(4, tMgr.getLine1Number().length());
//	    }
//	    
	    
	    //System.out.println("number:------"+ mPhoneNumber);
	    return mPhoneNumber;   
	}       
	
	private boolean isNetworkAvailable() {
		boolean connected = false ;
	        try {
	       ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
	        		
	        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	        connected = networkInfo != null && networkInfo.isAvailable() &&
	                networkInfo.isConnected();
	        return connected;


	        } catch (Exception e) {
	            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
	            Log.v("connectivity", e.toString());
	        }
	        return connected;
	    }
	
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		//System.out.println("DIALOG:-"+id);
		String ok = getString(R.string.ok);
		
		final AlertDialog alertDialog = new AlertDialog.Builder(ZiyarahMainActivity.this).create();
		switch(id) {
		
		
		
		case DIALOG_SENDING_REQ_STATUS:

			alertDialog.setTitle(R.string.SendingReqStatus);
			alertDialog.setMessage("Request has been send please wait for SMS.\nAfter Successfull SMS received please press Finish Button");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					Button step2 = (Button)findViewById(R.id.btnUpdateRequest);
					step2.setVisibility(View.VISIBLE);

				}
			});
			alertDialog.show();
			break;
		case 1:
			String strMessage1 = getString(R.string.enter_source_msisdn);
			
			final EditText text1 = (EditText)findViewById(R.id.etMSISDN);
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage1);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text1.setFocusable(true);
					text1.requestFocus();

				}
			});
			alertDialog.show();
			break;
		case 10:
			String strMessage10 = getString(R.string.enter_dest_msisdn);
			
			final EditText text10 = (EditText)findViewById(R.id.etMSISDNDestination);
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage10);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text10.setFocusable(true);
					text10.requestFocus();

				}
			});
			alertDialog.show();
			break;
		case 2:
			String strMessage2 = getString(R.string.enter_sim_number);
			final EditText text2 = (EditText)findViewById(R.id.etIMSI);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage2);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text2.setFocusable(true);
					text2.requestFocus();

				}
			});
			alertDialog.show();
			break;
		case 3:
			
			String strMessage3 = getString(R.string.enter_id_number);
			final EditText text3 = (EditText)findViewById(R.id.etIdNumber);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage3);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text3.setFocusable(true);
					text3.requestFocus();

				}
			});
			alertDialog.show();
			break;
		case 4:
			
			String strMessage4 = getString(R.string.enter_customer_first_name);
			final EditText text4 = (EditText)findViewById(R.id.etFirstName);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage4);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text4.setFocusable(true);
					text4.requestFocus();

				}
			});

			alertDialog.show();
			break;
		case 5:
			
			String strMessage5 = getString(R.string.enter_customer_last_name);
			final EditText text5 = (EditText)findViewById(R.id.etLastName);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage5);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					text5.setFocusable(true);
					text5.requestFocus();
				}
			});

			alertDialog.show();
			break;
		case 6:
			
			String strMessage6 = getString(R.string.select_photo);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage6);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();

				}
			});

			alertDialog.show();
			break;
		case 7:
			
			String strMessage7 = getString(R.string.enter_country);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage7);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();

				}
			});

			alertDialog.show();
			break;
		case 11:
			
			String strMessage11 = getString(R.string.select_cutomer_type);
			
			alertDialog.setTitle(R.string.missing_field);
			alertDialog.setMessage(strMessage11);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();

				}
			});

			alertDialog.show();
			break;
	
		case 8:
			alertDialog.setTitle(R.string.error);
			alertDialog.setMessage(errorMessage);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();

				}
			});

			alertDialog.show();
			break;
		case 12:
			alertDialog.setTitle(R.string.status);
			alertDialog.setMessage(errorMessage);
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					finish();

				}
			});

			alertDialog.show();
			break;
		case 9:
			alertDialog.setTitle(R.string.network);
			alertDialog.setMessage(this.getResources().getString(R.string.networkError));
			alertDialog.setButton(ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					alertDialog.cancel();
					//finish();

				}
			});

			alertDialog.show();
			break;
		default:
			dialog = null;
		}
		return alertDialog;
	}
	
	public void onSendRequest(View v){
		if (!verifyInputs())
			return;
		else
		{
		
		
		MSISDN = ((EditText)findViewById(R.id.etMSISDN)).getText().toString();
		simNumber =	((EditText)findViewById(R.id.etIMSI)).getText().toString();
		iDNumber =	((EditText)findViewById(R.id.etIdNumber)).getText().toString();
		customerFName = ((EditText)findViewById(R.id.etFirstName)).getText().toString();
		customerLName = ((EditText)findViewById(R.id.etLastName)).getText().toString();
		
		SawaZiyaraService se = new SawaZiyaraService();
		boolean error = false;
	//	try {
			LoadingTask lo = new LoadingTask();
			lo.execute();
		}

		
//		sendRequestViaSMS();
//		onCreateDialog(0);
		//showToast("Request Sent");
		//this.finish();
		//sendEmail();
		
		
		
		
//		AlertDialog ad = new AlertDialog.Builder(this).create();  
//		ad.setCancelable(false); // This blocks the 'BACK' button  
//		ad.setMessage("Backend is not working. Please try again later");  
//		ad.setButton("OK", new DialogInterface.OnClickListener() {  
//		    @Override  
//		    public void onClick(DialogInterface dialog, int which) {  
//		        dialog.dismiss();                      
//		    }  
//		});  
//		ad.show();  
		
	}
	
	
	boolean verifyInputs(){
		String errorMsg=null;
		MSISDN = ((EditText)findViewById(R.id.etMSISDN)).getText().toString();
		
		simNumber =	((EditText)findViewById(R.id.etIMSI)).getText().toString();
		
		destMSISDN = ((EditText)findViewById(R.id.etMSISDNDestination)).getText().toString();
		
		iDNumber =	((EditText)findViewById(R.id.etIdNumber)).getText().toString();
		
		customerFName = ((EditText)findViewById(R.id.etFirstName)).getText().toString();
		
		customerLName = ((EditText)findViewById(R.id.etLastName)).getText().toString();
		
		//System.out.println("******* simNumber:="+ simNumber);
		
		

		if (MSISDN.trim().equals("")){
			onCreateDialog(1);
			return false;
		}
		if (destMSISDN.trim().equals("")){
			onCreateDialog(10);
			return false;
		}
		else if(simNumber.trim().equals(""))
		{
			onCreateDialog(2);
			return false;
		}
		else if(cutomerTypePos == 0)
		{
			onCreateDialog(11);
			return false;
			
		}
		else if(iDNumber.trim().equals(""))
		{
			onCreateDialog(3);
			return false;
		}
		else if(customerFName.trim().equals(""))
		{
			onCreateDialog(4);
			return false;
		}
		else if(customerLName.trim().equals(""))
		{
			onCreateDialog(5);
			return false;
		}
		else if(selectPosition == 0)
		{
			onCreateDialog(7);
			return false;
		}
		else if(!idPhotoHasBeenTaken)
		{
			onCreateDialog(6);
			return false;
		}
		return true;
	}
	
	
//	void sendEmail(){
//		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//		String emailList[] = {"rs@wamsoft.ws"};  
//		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);  
//		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Register Num "+mobileNumber);  
//		emailIntent.putExtra(Intent.EXTRA_STREAM, idPhotoUri);
//		String emailBody="Mobile Number: "+mobileNumber+"\n"+
//				"IMSI: "+IMSI+"\n"+
//				"ID Number: "+IDNumber;
//		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,emailBody);  
//		emailIntent.setType("message/rfc822");  
//		startActivity(emailIntent);   
//		sendingProgress=SENDINGPROG_EMAIL_SUBMITTED;  
//	}

	
	public void onTakePhoto(View v){
		// create Intent to take a picture and return control to the calling application
		//
		//
		//		idPhotoUri = getOutputMediaFileUri(); // create a file to save the image
		//		showToast(idPhotoUri.toString());
		//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//		// MediaStore.
		//		intent.putExtra(MediaStore.EXTRA_OUTPUT, idPhotoUri); // set the image file name
		//		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,80000); 
		//		//start the image capture Intent
		//		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

		// take the photo
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 0);

	}
	
	
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		switch(requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				 f = new File(Environment.getExternalStorageDirectory()+ File.separator + "id.jpg");
				try {
				Bundle extras = data.getExtras();
				Bitmap bmp = (Bitmap) extras.get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				
				//you can create a new file name "test.jpg" in sdcard folder.
				
				f.createNewFile();
				//write the bytes in file
				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				fo.close();
				Log.d("DEBUG","SAVED TO:"+f.getAbsolutePath());
				}
				catch(Exception e) {
					e.printStackTrace();
				}
//				// REDUCE IMAGE
//				try {
//					String samplePhoto="file:///mnt/sdcard/Pictures/STCSimRegisterApp/TEST.jpg";
//					Uri fileUri = Uri.parse(samplePhoto);
//
//					Bitmap bm = ShrinkBitmap(idPhotoUri.toString(), 300, 300);
//					if(bm==null) Log.d("DEBUG","NULL");
//					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//					bm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//					//you can create a new file name "test.jpg" in sdcard folder.
//					File f = new File(Environment.getExternalStorageDirectory()+ File.separator + "test.jpg");
//					f.createNewFile();
//					//write the bytes in file
//					FileOutputStream fo = new FileOutputStream(f);
//					fo.write(bytes.toByteArray());
//					fo.close();
//					Log.d("DEBUG","SAVED TO:"+f.getAbsolutePath());
//					//MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
//				}catch(Exception e ) {
//					e.printStackTrace();
//				}

				idPhotoHasBeenTaken=true;
				showTakenPhotoInImageView(Uri.fromFile(f));
				//R.id.ivIDPhoto
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
				showToast("User cancelled the image capture");
			} else {
				// Image capture failed, advise user
				showToast("Image capture failed, advise user");
			}
		}
	}

	
	void showTakenPhotoInImageView(Uri f){
		ImageView imageView = (ImageView) findViewById(R.id.ivIDPhoto);
		imageView.setVisibility(View.VISIBLE);
		// Bitmap bm = ShrinkBitmap("/sdcard/Pictures/STCSimRegisterApp/IMG_STC.jpg", 800, 800);
		// imageView.setImageBitmap(bm);

		imageView.setImageURI(f);
		Button btn = (Button)findViewById(R.id.btnTakePhoto);
		btn.setEnabled(false);

	}
	
	void showToast(String text){
		Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
	}
	
	
	
	public void onClick(View v){
		SawaZiyaraService se = new SawaZiyaraService();
		boolean error = false;
	//	try {
			LoadingTask lo = new LoadingTask();
			lo.execute();
	/*	} catch (Exception e) {
			// TODO Auto-generated catch block
			error = true;
			onCreateDialog(5);
			
		}
		if(!error){
			finish();
		}*/
		//showToast("ON BUTTON CLICK");
	}

	
	
	
	public Bitmap ShrinkBitmap(String file, int width, int height){

		//System.out.println("SHRINK:"+file); 
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
		int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

		if (heightRatio > 1 || widthRatio > 1)
		{
			if (heightRatio > widthRatio)
			{
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio; 
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}



	private static Uri getOutputMediaFileUri(){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "STCSimRegisterApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile= new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ "STC" + ".jpg");


		return Uri.fromFile(mediaFile);
	}


		private class LoadingTask extends AsyncTask<Void, Void, Void> {

		private boolean networkError = false;
		private String response;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ZiyarahMainActivity.this, "", getString(R.string.loading), true, true, new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog) {
                	LoadingTask.this.cancel(true);
                   // finish();
                }
            });
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// request data

			SawaZiyaraService se = new SawaZiyaraService();
			boolean error = false;
			try {
				
//				System.out.println("MSISDN:="+ MSISDN);
//				System.out.println("simNumber:="+ simNumber);
//				System.out.println("iDNumber:="+ iDNumber);
//				System.out.println("customerFName:="+ customerFName);
//				System.out.println("customerLName:="+ customerLName);
//				System.out.println("country:="+ country);
				
				JSONObject obj2 = se.setHeadrs(f, MSISDN, simNumber, iDNumber,customerFName,customerLName,countryName,custmerTypeNmae,destMSISDN);
				
				JSONArray message = null;
				if(obj2 != null && obj2.toString() != null && !obj2.toString().equals(""))
				 message = obj2.getJSONArray("errors");
				else 
				{
				String msg = getString(R.string.successful_message);	
				errorMessage = msg;
				}
				
				System.out.println("Message:======"+ message);
				
				if(message != null && !message.equals(""))
				errorMessage = message.getJSONObject(0).getString("message");
				
				
			} catch (NetworkingException e) {
				networkError = true;
			}
			catch (ServiceNotAvailableException e) {
				internalError = true;
			}
			catch (Exception e) {
				e.printStackTrace();
				internalError = true;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void empty) {
			
			if (dialog.isShowing()) {
				dialog.dismiss();
				//finish();
			}
			
			if (networkError){
				onCreateDialog(9);
				
				//System.out.println("Network Error");
			}
			else if(internalError){
				onCreateDialog(8);
				
			}
			
			if(!networkError && !internalError)
			{
				onCreateDialog(12);
			}

		}

	}
	
		
		
		private class LoadingTaskForContent extends AsyncTask<Void, Void, Void> {


			private String response;

			@Override
			protected void onPreExecute() {
				dialog = ProgressDialog.show(ZiyarahMainActivity.this, "", getString(R.string.loadingCountries), true, true, new DialogInterface.OnCancelListener(){
	                @Override
	                public void onCancel(DialogInterface dialog) {
	                	LoadingTaskForContent.this.cancel(true);
	                    finish();
	                }
	            });
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				// request data

				SawaZiyaraService se = new SawaZiyaraService();
				boolean error = false;
				try {
					
//					System.out.println("MSISDN:="+ MSISDN);
//					System.out.println("simNumber:="+ simNumber);
//					System.out.println("iDNumber:="+ iDNumber);
//					System.out.println("customerFName:="+ customerFName);
//					System.out.println("customerLName:="+ customerLName);
//					System.out.println("country:="+ country);
					
					JSONObject obj = se.setHeadrsForGet();
					JSONArray nationalities = obj.getJSONArray("content");
					
					 
					 //System.out.println("Before nationalities------------->:="+ nationalities.length());
					 
					 for(int i=0; i<nationalities.length(); i++){
						 
						 countryNameArray.add(nationalities.getJSONObject(i).getString("message"));
					 }
					 
					 JSONObject myObjArr  = null; 
					 cRMID.add("Please select Country");
					 
					 for(int ind=0; ind<countryNameArray.size(); ind++){
						 myObjArr = new JSONObject(countryNameArray.get(ind));
						 countryNames.add(myObjArr.getString("nationality"));
						 cRMID.add(myObjArr.getString("CRMID"));
						 
					 }
					
				} catch (NetworkingException e) {
					networkError = true;
				}
				catch (ServiceNotAvailableException e) {
					internalError = true;
				}
				catch (Exception e) {
					e.printStackTrace();
					internalError = true;
				}
				

				return null;
			}

			@Override
			protected void onPostExecute(Void empty) {
				
				if (dialog.isShowing()) {
					dialog.dismiss();
					//finish();
				}
				
				if (networkError){
					onCreateDialog(9);
					
					//System.out.println("Network Error");
				}
				else if(internalError){
					onCreateDialog(8);
					
				}
				
				if(!networkError && !internalError)
				{
					// Get a reference to the AutoCompleteTextView in the layout
					//	AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
						// Get the string array
					//	String[] countries = getResources().getStringArray(R.array.countries_array);
						// Create the adapter and set it to the AutoCompleteTextView 
					//JSONObject myObjArr  = new JSONObject(countryNameArray.get(0));
					//	 System.out.println("**********************cRMID---------------->:="+ cRMID);
					//	 System.out.println("**********************countryName---------------->:="+ countryName);
				 
				
					ArrayAdapter<String> adapter = 
					        new ArrayAdapter<String>(ZiyarahMainActivity.this, android.R.layout.simple_dropdown_item_1line, cRMID);
					
					
					sp.setAdapter(adapter);
					
					//onCreateDialog(12);

					
				}
				

			}

		}	
	
	
	
	
	

}
