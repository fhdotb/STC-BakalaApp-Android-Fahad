package sa.com.stc.SimRegister;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stc.sim.utils.NetworkingException;
import com.stc.sim.utils.ServiceNotAvailableException;

import org.json.JSONObject;

/**
 * Created by Fahad Alotaibi on 06/04/14.
 */


public class StateQuery extends Activity {


    private EditText numberReg;
    private Button submit;
    String registrationNumber;

    private ProgressDialog dialog = null ;
    boolean internalError;

    String Language;

    private String Status;
    private String ProductName;
    private String ErrorDescription;

    String errorMessage = "Some problem occured please try again later";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Language = getIntent().getExtras().getString("Language");

        if(Language.equals("ARABIC")) {
            setContentView(R.layout.state_activaty_ar);
            ((TextView) findViewById(R.id.maintitle)).setText("حالة التسجيل");
        }
        else {
            setContentView(R.layout.state_activaty);
            ((TextView) findViewById(R.id.maintitle)).setText("State Registration");
        }

        numberReg = (EditText)this.findViewById(R.id.editText_reg);
        submit = (Button)this.findViewById(R.id.submit);



    }


    boolean verifyInput()
    {
        registrationNumber = numberReg.getText().toString();

        if(registrationNumber.equals("") || registrationNumber.trim() == null)
        {
            dialogErrorMessage();
            return false;
        }
        return true;
    }



    void dialogErrorMessage()
    {

        final AlertDialog alertDialog = new AlertDialog.Builder(StateQuery.this).create();
        alertDialog.setTitle("Field Empty");
        alertDialog.setMessage("Please enter the registration ID");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();

            }
        });
        alertDialog.show();

    }

    void dialogForRespons(int id)
    {
        final AlertDialog alertDialog;

        switch (id)
        {
            case 1: // correct case
                 alertDialog = new AlertDialog.Builder(StateQuery.this).create();
                 alertDialog.setTitle("Registration State");
                 if(ErrorDescription.equals(""))
                     alertDialog.setMessage("status : " + this.Status + "." + "\n" + "ProductName: " + this.ProductName + ".");
                else
                     alertDialog.setMessage("status : " + this.Status + "." + "\n" + "ProductName: " + this.ProductName + "." + "\n" + "errorDescription: " + this.ErrorDescription + ".");
                 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();

                        }
                    });
                    alertDialog.show();
                break;
            case 0:
                    alertDialog = new AlertDialog.Builder(StateQuery.this).create();
                    alertDialog.setTitle("Invalid Input");
                    alertDialog.setMessage("Input is incorrect, please try again.");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.cancel();

                        }
                    });
                    alertDialog.show();
                break;
            default:
        }

    }



    public void onClickQuery(View v)
    {
        if(!verifyInput())
            return;
        else
        {

            LoadingTask lo = new LoadingTask();
            lo.execute();

        }

    }



    private class LoadingTask extends AsyncTask<Void, Void, Void> {

        private boolean networkError = false;
        private String response;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(StateQuery.this, "", getString(R.string.loading), true, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    LoadingTask.this.cancel(true);
                    //  finish();
                }
            });
        }



        protected Void doInBackground(Void... arg0) {

            sawaBakalaService se = new sawaBakalaService();
            boolean error = false;


            try {
                  JSONObject obj2 = se.setHeadrsForGet(registrationNumber);
                  Status = obj2.getString("status");
                System.out.println("Status==="+Status);
                  ProductName = obj2.getString("productName");
                  ErrorDescription = obj2.getString("errorDescription");

                if(Status.equals("null") && ProductName.equals("null") && ErrorDescription.equals("null"))
                {
                   // dialogForRespons(0);
//                    Toast.makeText(getApplicationContext(),"null" ,Toast.LENGTH_LONG).show();
                }
                else
                {
                   // dialogForRespons(1);
               //     Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_LONG).show();
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
               // finish();
            }

            if(Status.equals("null") && ProductName.equals("null") && ErrorDescription.equals("null"))
            {
                 dialogForRespons(0);
//                    Toast.makeText(getApplicationContext(),"null" ,Toast.LENGTH_LONG).show();
            }
            else
            {
                 dialogForRespons(1);
                //     Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_LONG).show();
            }

            if (networkError){
                onCreateDialog(5);
            }


        }

    }


}





