package xyz.thetechninja.barcodeessential;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 200;
    public static final int PERMISSION_REQUEST_S = 100;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        final ScrollView mScrollView = (ScrollView) inflater.inflate(R.layout.fragment_main, container, false);

        Button supportButton = mScrollView.findViewById(R.id.supported_code);

        Button scanButton = mScrollView.findViewById(R.id.scan_code);

        Button feedbackButton = mScrollView.findViewById(R.id.feedback);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"techninja.xyz@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback | Barcode Essential");
                try {
                    startActivity(Intent.createChooser(i, "Select Email Client to send Feedback..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                } else {
                    Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
                    startActivityForResult(scanIntent, PERMISSION_REQUEST_S);
                }
            }
        });

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent supIntent = new Intent(getActivity(), SupportedTypes.class);
                startActivity(supIntent);
            }
        });

        // Inflate the layout for this fragment
        return mScrollView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            //Do Nothing
        } else {
            Bundle extras = data.getExtras();
            final String strtext = extras.getString("scanResult");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Scan Result");
            builder.setMessage(strtext);
            builder.setPositiveButton("Copy Scanned Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Code to Copy the content of Text View to the Clip board.
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Scan Result", strtext);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity().getApplicationContext(), "Scan Result Copied to Clipboard",
                            Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
