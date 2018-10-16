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
public class OverviewRitualsFragment extends Fragment {

    private ImageView imgOverviewRituals;

    public OverviewRitualsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview_rituals, container, false);

        imgOverviewRituals = (ImageView)view.findViewById(R.id.imgOverView);

        Picasso.with(getContext()).load(R.drawable.img_overview_rituals).into(imgOverviewRituals);

        return view;
    }

}
