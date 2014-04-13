package sa.com.stc.SimRegister;

/**
 * Created by Fahad Alotaibi on 08/04/14.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stc.sim.utils.NetworkingException;
import com.stc.sim.utils.ServiceNotAvailableException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InputFormActivity  extends Activity {
    static final int DIALOG_SENDING_REQ_STATUS = 0;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private Uri idPhotoUri;
    boolean idPhotoHasBeenTaken;

    String mobileNumber;
    String IMSI;
    String IDNumber;
    String idType;
    String date;

    String errorMessage = "Some problem occured please try again later";
    int sendingProgress=0;
    static final int SENDINGPROG_START=0;
    static final int SENDINGPROG_EMAIL_SUBMITTED=1;
    static final int SENDINGPROG_SMS_SUBMITTED=2;
    boolean internalError;
    File f ;
    String registrationMode = "";
    String modeStr = "";
    Bitmap bitmapImage;
    private ProgressDialog dialog = null ;

    RadioGroup radioGroup;
    Button registration;

    Calendar myCalendar;
    EditText editText ;
    String Language;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Language = getIntent().getExtras().getString("Language");

        if(Language.equals("ARABIC"))
                setContentView(R.layout.input_form_ar);
        else
            setContentView(R.layout.input_form);


        if (!isNetworkAvailable()){
            onCreateDialog(6);
        }

        registration =(Button)this.findViewById(R.id.btnSendRequest);
        radioGroup= (RadioGroup)findViewById(R.id.radios);
        registrationMode = getIntent().getExtras().getString("REGISTERATION_MODE");
        //showToast(registrationMode);

        Button regButton = (Button)findViewById(R.id.btnSendRequest);
        Button topButton = (Button)findViewById(R.id.topbarButton);

        if(registrationMode.equals("REGISTER") && Language.equals("ARABIC") )
        {
            modeStr = "     تسجيل الشريحة";
            regButton.setText("تسجيل");
            ((TextView) findViewById(R.id.maintitle)).setText(modeStr);
        }
        else
        {
            modeStr = "     Register SIM";
            regButton.setText("Register");
            ((TextView) findViewById(R.id.maintitle)).setText(modeStr);
        }

		/*for simulation*/
      //  fillSampleInpuData();

        editText = (EditText) findViewById(R.id.editDate1);

         myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InputFormActivity.this, dateDialog , myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




    }


    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }



   public String getRadio()
    {
        int id = radioGroup.getCheckedRadioButtonId();
        if(id == R.id.redio_1)
            return "S";
        else
            return "SN";


    }


    public void reLoadActivaty(View v)
    {
        ((EditText)findViewById(R.id.etMobileNumber)).setText("");
        ((EditText)findViewById(R.id.etIMSI)).setText("");
        ((EditText)findViewById(R.id.etIdNumber)).setText("");
        ((EditText)findViewById(R.id.editDate1)).setText("");
        ((Button)findViewById(R.id.btnTakePhoto)).setEnabled(true);
        ((Button)findViewById(R.id.display_imagSign)).setEnabled(false);
        ((ImageView)findViewById(R.id.ivIDPhoto)).setVisibility(View.INVISIBLE);
        ((RadioButton)findViewById(R.id.redio_1)).setChecked(true);
        ((RadioButton)findViewById(R.id.redio_2)).setChecked(false);
    }


    public void onClickStateRE(View v)
    {
        Intent i = new Intent (InputFormActivity.this , StateQuery.class);
        i.putExtra("Language", Language);
        startActivity(i);
        finish();
    }


    public void onClickImage(View v)
    {

        Intent i = new Intent (InputFormActivity.this , ImageClass.class);
        i.putExtra("BitmapImage",bitmapImage);
        startActivity(i);
        finish();

    }


    public void onClickRegistrationUpdate(View v)
    {
        if (!verifyInputs())
            return;
        else
        {
          //  sawaBakalaService se = new sawaBakalaService();
            LoadingTask lo = new LoadingTask();
            lo.execute();

        }


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
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }




    void fillSampleInpuData(){
        //String samplePhoto="file:///mnt/sdcard/Pictures/STCSimRegisterApp/IMG_20120714_130159.jpg";
        //idPhotoUri=Uri.parse(samplePhoto);
        //idPhotoHasBeenTaken=true;
        //showTakenPhotoInImageView();
        //	((EditText)findViewById(R.id.etMobileNumber)).setText("0554201070");
        //  ((EditText)findViewById(R.id.etIMSI)).setText("899660");
        //	((EditText)findViewById(R.id.etIdNumber)).setText("2010003232");
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        System.out.println("DIALOG:-"+id);
        final AlertDialog alertDialog = new AlertDialog.Builder(InputFormActivity.this).create();
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
                alertDialog.setTitle("Missing Field");
                alertDialog.setMessage("Please enter Mobile Number.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });
                alertDialog.show();
                break;

            case 2:
                alertDialog.setTitle("Missing Field");
                alertDialog.setMessage("Please enter SIM Number.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });
                alertDialog.show();
            case 3:
                alertDialog.setTitle("Missing Field");
                alertDialog.setMessage("Please enter ID/Passport Number.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });
                alertDialog.show();
                break;
            case 4:
                alertDialog.setTitle("Missing Field");
                alertDialog.setMessage("Please enter ID Photo. Please take the photo by pressing Take Photo button.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
                break;
            case 7:
                alertDialog.setTitle("Missing Field");
                alertDialog.setMessage("Please enter Birth Date.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
                break;
            case 5:
                alertDialog.setTitle("Network Error");
                alertDialog.setMessage(this.getResources().getString(R.string.networkError));
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
                break;

            case 6:
                alertDialog.setTitle("Network Error");
                alertDialog.setMessage(this.getResources().getString(R.string.networkError));
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.cancel();
                        finish();

                    }
                });

                alertDialog.show();
                break;
            default:
                dialog = null;
        }
        return alertDialog;
    }

    protected void onRestart (){
        super.onRestart();
        if (sendingProgress==SENDINGPROG_EMAIL_SUBMITTED){
            dismissDialog(DIALOG_SENDING_REQ_STATUS);
            String messagelBody="Mobile Number: "+mobileNumber+"\n"+
                    "IMSI: "+IMSI+"\n"+
                    "ID Number: "+IDNumber;
            sendSMS("0554201070",messagelBody);
            //showToast(messagelBody);
            onCreateDialog(0);
            sendingProgress=SENDINGPROG_SMS_SUBMITTED;
            this.finish();

        }
    }


    void sendRequestViaSMS(){
        String messagelBody = "";
        if(registrationMode.equals("REGISTER")){
            messagelBody="800830 "+IDNumber+" "+mobileNumber+" "+IMSI;
            showToast(messagelBody);
        }
        else {messagelBody="800830 "+mobileNumber+" "+IMSI;
            showToast(messagelBody);
        }
        //sendSMS("0554201070",messagelBody);
        sendSMS("902",messagelBody);
    }

    public void onSendRequest(View v){
        if (!verifyInputs())
            return;

        sendRequestViaSMS();
        onCreateDialog(0);
        //showToast("Request Sent");
        //this.finish();
        //sendEmail();
    }

    boolean verifyInputs(){
        String errorMsg=null;

        mobileNumber=((EditText)findViewById(R.id.etMobileNumber)).getText().toString();
        IMSI=((EditText)findViewById(R.id.etIMSI)).getText().toString();
        IDNumber=((EditText)findViewById(R.id.etIdNumber)).getText().toString();
        date = ((EditText)findViewById(R.id.editDate1)).getText().toString();
        idType= getRadio();

        if (mobileNumber.trim().equals("")){
            onCreateDialog(1);
            return false;
        }
        else if(IMSI.trim().equals(""))
        {
            onCreateDialog(2);
            return false;
        }
        else if(IDNumber.trim().equals(""))
        {
            onCreateDialog(3);
            return false;
        }
        else if(date.trim().equals(""))
        {
            onCreateDialog(7);
            return false;
        }
        else if(!idPhotoHasBeenTaken)
        {
            onCreateDialog(4);
            return false;
        }
        return true;
    }





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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.RootView));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    void sendEmail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String emailList[] = {"rs@wamsoft.ws"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Register Num "+mobileNumber);
        emailIntent.putExtra(Intent.EXTRA_STREAM, idPhotoUri);
        String emailBody="Mobile Number: "+mobileNumber+"\n"+
                "IMSI: "+IMSI+"\n"+
                "ID Number: "+IDNumber;
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,emailBody);
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
        sendingProgress=SENDINGPROG_EMAIL_SUBMITTED;
    }

    private void sendSMS(String phoneNumber, String message)
    {
        Log.v("phoneNumber", phoneNumber);
        Log.v("MEssage", message);
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, Object.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }

    public void onClick(View v){
        SendEmail se = new SendEmail();
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

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        switch(requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    f = new File(Environment.getExternalStorageDirectory()+ File.separator + "id.jpg");
                    try {
                        Bundle extras = data.getExtras();
                        Bitmap bmp = (Bitmap) extras.get("data");
                        bitmapImage = bmp;
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                        //you can create a new file name "test.jpg" in sdcard folder.

                        f.createNewFile();
                        //write the bytes in file
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Log.d("DEBUG","SAVED TO:"+f.getAbsolutePath());


                        ((ImageView)findViewById(R.id.ivIDPhoto)).setVisibility(View.VISIBLE);
                        ((Button)findViewById(R.id.display_imagSign)).setEnabled(true);
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

    public Bitmap ShrinkBitmap(String file, int width, int height){

        System.out.println("SHRINK:"+file);
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


    void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    /*public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }*/
    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        private boolean networkError = false;
        private String response;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(InputFormActivity.this, "", getString(R.string.loading), true, true, new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog) {
                    LoadingTask.this.cancel(true);
                  //  finish();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            sawaBakalaService se = new sawaBakalaService();
            boolean error = false;

            try {

                JSONObject obj2 = se.setHeadrs(f, mobileNumber,IMSI, IDNumber, idType, date);
                JSONArray message = null;

                if(obj2 != null && obj2.toString() != null && !obj2.toString().equals(""))
                    message = obj2.getJSONArray("errors");
                else
                {
                    String msg = getString(R.string.successful_message);
                    errorMessage = msg;
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
            //  }

            return null;
        }

        @Override
        protected void onPostExecute(Void empty) {

            if (dialog.isShowing()) {
                dialog.dismiss();
                finish();
            }

            if (networkError){
                onCreateDialog(5);
            }


        }

    }

}
