package zamboanga.antao.pockethajj.StepByStepHajj;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepOneFragment extends Fragment {

    private ImageView imgPartOneArrival;
    private ImageView imgPartOneIhram;

    public StepOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_step_one, container, false);

        imgPartOneArrival = (ImageView) view.findViewById(R.id.img_partOne_arrival);
        imgPartOneIhram = (ImageView) view.findViewById(R.id.img_partOne_ihram);

        Picasso.with(getActivity()).load(R.drawable.img_step_one_arrival).into(imgPartOneArrival);
        Picasso.with(getActivity()).load(R.drawable.img_step_one_ihram).into(imgPartOneIhram);

        return view;
    }

}
