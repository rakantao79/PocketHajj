package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.Payments.ViewDetailPendingPaymentActivity;
import zamboanga.antao.pockethajj.R;

public class PendingPaymentsActivity extends AppCompatActivity {

    private RecyclerView viewPendingRecyclerView;

    private DatabaseReference mPendingPaymentDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_payments);

        viewPendingRecyclerView = (RecyclerView) findViewById(R.id.viewPendingPayments_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mPendingPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(mCurrent_user_id);

        viewPendingRecyclerView.setHasFixedSize(true);
        viewPendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataPayment, viewPendingPaymentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataPayment, viewPendingPaymentsViewHolder>(

                        DataPayment.class,
                        R.layout.user_payments,
                        viewPendingPaymentsViewHolder.class,
                        mPendingPaymentDatabase.orderByKey()

                ) {
            @Override
            protected void populateViewHolder(viewPendingPaymentsViewHolder viewHolder, DataPayment model, int position) {

                viewHolder.setAmount(model.getAmount());
                viewHolder.setDateUpload(model.getDateupload());
                viewHolder.setScreenshoturl(model.getScreenshoturl(), getApplicationContext());

                final String list_user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentViewPendingPayment = new Intent(PendingPaymentsActivity.this, ViewDetailPendingPaymentActivity.class);
                        intentViewPendingPayment.putExtra("push_key", list_user_id);
                        startActivity(intentViewPendingPayment);

                    }
                });
            }
        };

        viewPendingRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class viewPendingPaymentsViewHolder extends RecyclerView.ViewHolder {

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        View mView;

        public viewPendingPaymentsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setAmount(Double amount){
            TextView tvPaymentAmount = (TextView) mView.findViewById(R.id.tvPaymentAmount);
            //tvPaymentAmount.setText(String.valueOf(amount));
            tvPaymentAmount.setText(decimalFormat.format(amount));
        }

        public void setScreenshoturl(String screenshoturl, Context context){
            ImageView imgScreenshot = (ImageView) mView.findViewById(R.id.img_user_Screenshot);
            Picasso.with(context).load(screenshoturl).placeholder(R.drawable.img_profile_img).into(imgScreenshot);
        }

        public void setDateUpload (String dateUpload){
            TextView tvDateupload = (TextView) mView.findViewById(R.id.tvDateUpload);
            tvDateupload.setText(dateUpload);
        }
    }
}
