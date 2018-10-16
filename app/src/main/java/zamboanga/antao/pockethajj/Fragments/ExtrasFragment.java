package zamboanga.antao.pockethajj.Fragments;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import zamboanga.antao.pockethajj.Extras.HajjVRActivity;
import zamboanga.antao.pockethajj.Extras.QiblaActivity;
import zamboanga.antao.pockethajj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExtrasFragment extends Fragment {

    private ImageView imgVirtualTour;
    private ImageView imgQibla;




    public ExtrasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_extras, container, false);

        imgVirtualTour = (ImageView) view.findViewById(R.id.imgVirtualTour);
        imgQibla = (ImageView) view.findViewById(R.id.imgQibla);

        imgVirtualTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), HajjVRActivity.class);
                //startActivity(intent);
                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.google.devrel.vrviewapp");
                startActivity(intent);

            }
        });


        imgQibla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QiblaActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
