package zamboanga.antao.pockethajj.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import zamboanga.antao.pockethajj.R;
import zamboanga.antao.pockethajj.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {


    private Button btnCallStartActivity;


    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        btnCallStartActivity = (Button) view.findViewById(R.id.btnCallStartActivity);

        btnCallStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
