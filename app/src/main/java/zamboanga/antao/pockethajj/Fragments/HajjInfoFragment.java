package zamboanga.antao.pockethajj.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import zamboanga.antao.pockethajj.R;
import zamboanga.antao.pockethajj.StepByStepHajj.StepByStepHajjActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HajjInfoFragment extends Fragment {

    VideoView vvHajj;
    private Button btn_stepbystep;


    public HajjInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hajj_info, container, false);

        vvHajj = (VideoView)view.findViewById(R.id.vvHajj);
        btn_stepbystep = (Button)view.findViewById(R.id.btn_stepbystephajj);

        btn_stepbystep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StepByStepHajjActivity.class));
            }
        });

        playVideo();

        return view;
    }

    private void playVideo() {

        String videoHajj = "android.resource://" + getActivity().getPackageName() +"/"+R.raw.videohajj;
        MediaController mediaController = new MediaController(getActivity());

        vvHajj.setVideoURI(Uri.parse(videoHajj));
        vvHajj.setMediaController(mediaController);
        vvHajj.requestFocus();

    }
}
