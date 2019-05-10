package xyz.thetechninja.barcodeessential;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratorFragment extends Fragment {

    EditText text2conv;

    String gettext = "";

    TextView error_Text;

    public GeneratorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ScrollView mScrollView = (ScrollView) inflater.inflate(R.layout.fragment_generator, container, false);

        Button generator = mScrollView.findViewById(R.id.generate_code);

        text2conv = mScrollView.findViewById(R.id.input_text);

        error_Text = mScrollView.findViewById(R.id.errorText);

        generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text2conv.getText().toString().matches("")){
                    error_Text.setText("*This Field is required.");
                    error_Text.setTextColor(Color.RED);
                }
                else{
                    error_Text.setText("");
                    gettext = text2conv.getText().toString().trim();
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    intent.putExtra("mytext", gettext);
                    startActivity(intent);
                }
            }
        });

        // Inflate the layout for this fragment
        return mScrollView;
    }
}
