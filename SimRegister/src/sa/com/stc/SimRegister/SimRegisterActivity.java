package sa.com.stc.SimRegister;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SimRegisterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
    	Button btn = (Button)findViewById(R.id.topbarButton);
		btn.setText(R.string.sawa_bakala);
	
        
    }
    
    
    public void onClickHandler(View target) {
		switch (target.getId()) {
			case R.id.btnRegisterSim:
			{
				onShowInputForm(target,"REGISTER");
				break;
			}
			case R.id.btnUpdateSim:
			{
				onShowInputForm(target,"UPDATE");
				break;
			}
		default: System.out.println("DEBUG:-"+target.getId());
		}
	}
    
    public void onShowInputForm(View v, String registrationMode){
    	//setContentView(R.layout.input_form);
    	Intent intent = new Intent(this, InputFormActivity.class);
    	intent.putExtra("REGISTERATION_MODE", registrationMode);
    	startActivity(intent);
    }
}