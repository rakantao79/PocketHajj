package zamboanga.antao.pockethajj.StepByStepHajj;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Step1Fragment extends Fragment {


    public Step1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_step1, container, false);

        return view;
    }

}
