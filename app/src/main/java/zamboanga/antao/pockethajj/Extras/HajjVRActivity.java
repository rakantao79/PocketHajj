package zamboanga.antao.pockethajj.Extras;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.util.Locale;

import zamboanga.antao.pockethajj.R;

public class HajjVRActivity extends AppCompatActivity {

    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    private static final String STATE_PROGRESS_TIME = "progressTime";

    private VrVideoView videoWidgetView;
    private SeekBar seekBar;
    private TextView statusText;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hajj_vr);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        statusText = (TextView) findViewById(R.id.status_text);
        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);

        if (savedInstanceState != null){
            long progreTime = savedInstanceState.getLong(STATE_PROGRESS_TIME);
            videoWidgetView.seekTo(progreTime);
            seekBar.setMax((int)savedInstanceState.getLong(STATE_VIDEO_DURATION));
            seekBar.setProgress((int)progreTime);

            isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED);
            if (isPaused){
                videoWidgetView.pauseVideo();
            } else {
                seekBar.setEnabled(false);
            }

        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // if the user changed the position, seek to the new position.
                if (fromUser) {
                    videoWidgetView.seekTo(progress);
                    updateStatusText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // ignore for now.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // ignore for now.
            }
        });

        videoWidgetView.setEventListener(new VrVideoEventListener(){
            @Override
            public void onLoadSuccess() {
                //Log.i(TAG, "Successfully loaded video " + videoWidgetView.getDuration());
                seekBar.setMax((int) videoWidgetView.getDuration());
                seekBar.setEnabled(true);
                updateStatusText();
            }

            @Override
            public void onLoadError(String errorMessage) {
                Toast.makeText(HajjVRActivity.this, "Error loading video: " + errorMessage, Toast.LENGTH_LONG).show();
                //Log.e(TAG, "Error loading video: " + errorMessage);
            }

            @Override
            public void onClick() {
                if (isPaused) {
                    videoWidgetView.playVideo();
                } else {
                    videoWidgetView.pauseVideo();
                }

                isPaused = !isPaused;
                updateStatusText();
            }

            @Override
            public void onNewFrame() {
                updateStatusText();
                seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
            }

            @Override
            public void onCompletion() {
                videoWidgetView.seekTo(0);
            }
        });

    }

    private void  updateStatusText(){
        String status = (isPaused ? "Paused: " : "Playing: ") +
                String.format(Locale.getDefault(), "%.2f", videoWidgetView.getCurrentPosition() / 1000f) +
                " / " +
                videoWidgetView.getDuration() / 1000f +
                " seconds.";
        statusText.setText(status);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_PROGRESS_TIME, videoWidgetView.getCurrentPosition());
        outState.putLong(STATE_VIDEO_DURATION, videoWidgetView.getDuration());
        outState.putBoolean(STATE_IS_PAUSED, isPaused);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        videoWidgetView.pauseRendering();
        isPaused = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        videoWidgetView.resumeRendering();
        updateStatusText();
    }

    @Override
    public void onDestroy() {
        videoWidgetView.shutdown();
        super.onDestroy();

    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.devrel.vrviewapp");
        startActivity(intent);

    }
}
