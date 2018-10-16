package zamboanga.antao.pockethajj.PilgrimUpdate;

import android.content.Context;
import android.content.Intent;

import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;

import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.Payments.ViewDetailPaymentActivity;
import zamboanga.antao.pockethajj.R;

public class UpdatePaymentActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private DatabaseReference mPendingPaymentsDatabase;

    private RecyclerView pendingPaymentsRecyclerview;

    private TextView tvPendingAmountTotal;

    private TextView tvPilgrimTotalPaid;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private Double TotalPrice = 0.0;
    private Double Balance = 0.0;

    DecimalFormat decimalFormat;

    private Toolbar mToolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_payment);

        decimalFormat = new DecimalFormat("#,###,###.00");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_updatepayment);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Payments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvPendingAmountTotal = (TextView) findViewById(R.id.tvPendingAmountTotal) ;
        tvPilgrimTotalPaid = (TextView) findViewById(R.id.tvPilgrimTotalPaid) ;


        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mPendingPaymentsDatabase = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments").child(mCurrent_user_id);

        pendingPaymentsRecyclerview = (RecyclerView) findViewById(R.id.pending_payments_recyclerview);
        pendingPaymentsRecyclerview.setHasFixedSize(true);
        pendingPaymentsRecyclerview.setLayoutManager(new LinearLayoutManager(UpdatePaymentActivity.this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_view_pending_payments, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        int id  = item.getItemId();

        if (id == R.id.action_view_pending_payments){
            startActivity(new Intent(UpdatePaymentActivity.this, PendingPaymentsActivity.class));
            finish();
            return true;
        }
        if (id == R.id.action_add_payment){
            startActivity(new Intent(UpdatePaymentActivity.this, UploadPaymentActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter <DataPayment, pendingPaymentsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataPayment, pendingPaymentsViewHolder>(

                        DataPayment.class,
                        R.layout.user_payments,
                        pendingPaymentsViewHolder.class,
                        mPendingPaymentsDatabase.orderByKey()

                ) {
            @Override
            protected void populateViewHolder(pendingPaymentsViewHolder viewHolder, final DataPayment model, int position) {

                viewHolder.setAmount(model.getAmount());
                viewHolder.setDateUpload(model.getDateupload());
                viewHolder.setScreenshoturl(model.getScreenshoturl(), getApplicationContext());

                mPendingPaymentsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //get the total amount of pending payments

                        if (dataSnapshot.exists()){
                            TotalPrice = TotalPrice + model.getAmount();
                            Balance = 200000 - TotalPrice;

                            //tvPendingAmountTotal.setText("Total Amount = " + String.format("%.2f",TotalPrice));
                            tvPendingAmountTotal.setText("Total Paid : " + decimalFormat.format(TotalPrice));

                            tvPilgrimTotalPaid.setText("Balance : " + decimalFormat.format(Balance));

                            if (TotalPrice >= 200000){
                                tvPilgrimTotalPaid.setText("Fully Paid");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final String list_user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentViewPendingPayment = new Intent(UpdatePaymentActivity.this, ViewDetailPaymentActivity.class);
                        intentViewPendingPayment.putExtra("user_id", list_user_id);
                        startActivity(intentViewPendingPayment);
                        finish();
                    }
                });

            }
        };

        pendingPaymentsRecyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    public static class pendingPaymentsViewHolder extends RecyclerView.ViewHolder {

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        View mView;

        public pendingPaymentsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setAmount(Double amount){
            TextView tvPaymentAmount = (TextView) mView.findViewById(R.id.tvPaymentAmount);
            //tvPaymentAmount.setText(String.valueOf(amount));
            tvPaymentAmount.setText(decimalFormat.format(amount));
        }

        public void setDateUpload (String dateUpload){
            TextView tvDateupload = (TextView) mView.findViewById(R.id.tvDateUpload);
            tvDateupload.setText(dateUpload);
        }

        public void setScreenshoturl(String screenshoturl, Context context){
            ImageView imgScreenshot = (ImageView) mView.findViewById(R.id.img_user_Screenshot);
            Picasso.with(context).load(screenshoturl).placeholder(R.drawable.img_profile_img).into(imgScreenshot);
        }
    }
}
