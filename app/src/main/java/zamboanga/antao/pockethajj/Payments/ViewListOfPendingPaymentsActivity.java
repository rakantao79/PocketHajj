package zamboanga.antao.pockethajj.Payments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.PilgrimUpdate.PendingPaymentsActivity;
import zamboanga.antao.pockethajj.PilgrimUpdate.ViewPendingPaymentActivity;
import zamboanga.antao.pockethajj.R;

public class ViewListOfPendingPaymentsActivity extends AppCompatActivity {

    private RecyclerView listOfPendingPaymentsRecyclerView;
    private DatabaseReference mPendingPaymentDatabase;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_pending_payments);

        user_id = getIntent().getStringExtra("user_id");

        listOfPendingPaymentsRecyclerView = (RecyclerView) findViewById(R.id.list_pending_payments_recyclerview);

        mPendingPaymentDatabase = FirebaseDatabase.getInstance().getReference().child("PendingPayments").child(user_id);

        listOfPendingPaymentsRecyclerView.setHasFixedSize(true);
        listOfPendingPaymentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataPayment, listOfPendingPaymentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataPayment, listOfPendingPaymentsViewHolder>(

                        DataPayment.class,
                        R.layout.user_payments,
                        listOfPendingPaymentsViewHolder.class,
                        mPendingPaymentDatabase

                ) {
            @Override
            protected void populateViewHolder(listOfPendingPaymentsViewHolder viewHolder, DataPayment model, int position) {

                viewHolder.setAmount(model.getAmount());
                viewHolder.setDateUpload(model.getDateupload());
                viewHolder.setScreenshoturl(model.getScreenshoturl(), getApplicationContext());

                final String pushkey = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentViewPendingPayment = new Intent(ViewListOfPendingPaymentsActivity.this, ViewPendingPaymentActivity.class);
                        intentViewPendingPayment.putExtra("pushkey", pushkey);
                        intentViewPendingPayment.putExtra("user_id", user_id);
                        finish();
                        startActivity(intentViewPendingPayment);

                    }
                });
            }
        };

        listOfPendingPaymentsRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class listOfPendingPaymentsViewHolder extends RecyclerView.ViewHolder {

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        View mView;

        public listOfPendingPaymentsViewHolder(View itemView) {

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
            Picasso.with(context).load(screenshoturl).placeholder(R.drawable.ic_center_focus_weak_black_24dp).into(imgScreenshot);
        }

        public void setDateUpload (String dateUpload){
            TextView tvDateupload = (TextView) mView.findViewById(R.id.tvDateUpload);
            tvDateupload.setText(dateUpload);
        }

    }

}
