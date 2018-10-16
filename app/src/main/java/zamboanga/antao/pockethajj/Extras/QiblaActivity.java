package zamboanga.antao.pockethajj.Extras;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zamboanga.antao.pockethajj.R;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener{

    ImageView iv_arrow;
    TextView degrees;
    private Button btn_backMain;

    private static SensorManager sensorService;
    private Sensor sensor;

    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);

        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        degrees = (TextView)findViewById(R.id.tvDegrees);
        btn_backMain = (Button)findViewById(R.id.btn_Back);

        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorService.getDefaultSensor(Sensor.TYPE_ORIENTATION);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensor != null) {
            sensorService.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(QiblaActivity.this, "Not Supported", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorService.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int degree = Math.round(event.values[0]);

        degrees.setText(Integer.toString(degree) + (char) 0x00b0);

        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setFillAfter(true);

        iv_arrow.setAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
