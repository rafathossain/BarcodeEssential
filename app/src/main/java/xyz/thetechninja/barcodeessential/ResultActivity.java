package xyz.thetechninja.barcodeessential;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static xyz.thetechninja.barcodeessential.MainFragment.PERMISSION_REQUEST_S;

public class ResultActivity extends AppCompatActivity {

    ImageView img;

    Button save;

    Bitmap bitmap;

    public static Bitmap viewToBitmap(View view, int width, int height){
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        img = findViewById(R.id.resimg);

        save = findViewById(R.id.save);

        Intent i = getIntent();
        String data = i.getStringExtra("mytext");
        data = data.trim();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ResultActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_S);
                }else{
                    FileOutputStream fileOutputStream = null;
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmhhmmss");
                    String date = simpleDateFormat.format(new Date());
                    String name = "Img"+date+".jpg";
                    String file_name = file.getAbsolutePath()+"/"+name;
                    File new_file = new File(file_name);

                    try {
                        fileOutputStream = new FileOutputStream(new_file);
                        Bitmap bt = viewToBitmap(img, img.getWidth(), img.getHeight());
                        bt.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Toast.makeText(ResultActivity.this, "Barcode Saved to Gallery", Toast.LENGTH_SHORT).show();
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 300: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    FileOutputStream fileOutputStream = null;
                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmhhmmss");
                    String date = simpleDateFormat.format(new Date());
                    String name = "Img"+date+".jpg";
                    String file_name = file.getAbsolutePath()+"/"+name;
                    File new_file = new File(file_name);

                    try {
                        fileOutputStream = new FileOutputStream(new_file);
                        Bitmap bt = viewToBitmap(img, img.getWidth(), img.getHeight());
                        bt.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        Toast.makeText(ResultActivity.this, "Barcode Saved to Gallery", Toast.LENGTH_SHORT).show();
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ResultActivity.this, "Permission denied to read your storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
