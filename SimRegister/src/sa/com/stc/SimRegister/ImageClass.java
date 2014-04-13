package sa.com.stc.SimRegister;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Fahad Alotaibi on 06/04/14.
 */
public class ImageClass extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.image_activaty);
        super.onCreate(savedInstanceState);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            Bitmap imageToDisplay = (Bitmap) b.get("BitmapImage");
            ImageView image = (ImageView)this.findViewById(R.id.widget10);
            image.setImageBitmap(imageToDisplay);

        }
        else
        {
            Toast.makeText(getApplicationContext(),"error " ,Toast.LENGTH_LONG).show();
        }

    }
}