package xyz.thetechninja.barcodeessential;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SupportedTypes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supported_types);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
