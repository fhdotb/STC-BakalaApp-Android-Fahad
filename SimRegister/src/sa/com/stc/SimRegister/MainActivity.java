package sa.com.stc.SimRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity {

    RadioGroup radios;
    String Language="ARABIC";
    Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        radios = (RadioGroup)this.findViewById(R.id.Radios);
        spinner = (Spinner)findViewById(R.id.topbarSpinner);

        ((Button)findViewById(R.id.btnSawaZiyarah)).setText("سوا زيارة");
        ((Button)findViewById(R.id.btnSawaBaqala)).setText("سوا بقالة");
        ((TextView) findViewById(R.id.maintitle)).setText("STC تسجيل الشريحة");



        // click on ENGLISH
        ((RadioButton)this.findViewById(R.id.redio_En)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Button) findViewById(R.id.btnSawaZiyarah)).setText("SAWA Ziyarah");
                ((Button) findViewById(R.id.btnSawaBaqala)).setText("SAWA Bakala");
                ((TextView) findViewById(R.id.maintitle)).setText("STC SIM Registration");


            }
        });

        // click on ARABIC
        ((RadioButton)this.findViewById(R.id.redio_AR)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Button)findViewById(R.id.btnSawaZiyarah)).setText("سوا زيارة");
                ((Button)findViewById(R.id.btnSawaBaqala)).setText("سوا بقالة");
                ((TextView) findViewById(R.id.maintitle)).setText("STC تسجيل الشريحة");

            }
        });
		
		fillGui();

	}

    void setRadio()
    {
        int id = radios.getCheckedRadioButtonId();
        if(id == R.id.redio_En)
            Language = "ENGLISH";
        else
            Language =  "ARABIC";
    }
	
	
	public void fillGui(){
		Button ziyarahBtn = (Button)findViewById(R.id.btnSawaZiyarah);
		ziyarahBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goZiyarahActivity(v);
				
			}
		});
		
		
		
		Button bakalaBtn = (Button)findViewById(R.id.btnSawaBaqala);
		bakalaBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				goBakalaActity(v);
			}
		});
		
		
		
	}
	
	
	public void goBakalaActity(View target){

	  //  Intent intent = new Intent(this, SimRegisterActivity.class);
	  //  startActivity(intent);

        setRadio();

        Intent intent = new Intent(this, InputFormActivity.class);
        intent.putExtra("REGISTERATION_MODE","REGISTER");
        intent.putExtra("Language", Language);
        startActivity(intent);

	}
	
	
	public void goZiyarahActivity(View v){

        setRadio();

		Intent intent = new Intent(this, ZiyarahMainActivity.class);
        intent.putExtra("Language", Language);
		startActivity(intent);
		
		
		
	}


}
