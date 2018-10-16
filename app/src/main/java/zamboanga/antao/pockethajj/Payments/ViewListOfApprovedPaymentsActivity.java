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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import zamboanga.antao.pockethajj.DataUsers.DataPayment;
import zamboanga.antao.pockethajj.R;

public class ViewListOfApprovedPaymentsActivity extends AppCompatActivity {

    private RecyclerView viewListofApprovedPaymeentsRecyclerview;

    private DatabaseReference mApprovedPaymentsDatabase;

    private String user_id;

    private Double TotalPrice = 0.0;
    private Double Balance = 0.0;

    private TextView tvTotalAmount;
    private TextView tvAmountBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_of_approved_payments);

        user_id = getIntent().getStringExtra("user_id");

        mApprovedPaymentsDatabase = FirebaseDatabase.getInstance().getReference().child("ApprovedPayments").child(user_id);

        viewListofApprovedPaymeentsRecyclerview = (RecyclerView) findViewById(R.id.viewListofApprovedPaymeentsRecyclerview);
        viewListofApprovedPaymeentsRecyclerview.setHasFixedSize(true);
        viewListofApprovedPaymeentsRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        tvTotalAmount = (TextView) findViewById(R.id.tvApprovedAmountTotal);
        tvAmountBalance = (TextView) findViewById(R.id.tvAmountBalance);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataPayment, viewListofApprovedPaymetnsViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataPayment, viewListofApprovedPaymetnsViewholder>(

                        DataPayment.class,
                        R.layout.user_payments,
                        viewListofApprovedPaymetnsViewholder.class,
                        mApprovedPaymentsDatabase

                ) {
            @Override
            protected void populateViewHolder(viewListofApprovedPaymetnsViewholder viewHolder, final DataPayment model, int position) {

                viewHolder.setAmount(model.getAmount());
                viewHolder.setDateUpload(model.getDateupload());
                viewHolder.setScreenshoturl(model.getScreenshoturl(), getApplicationContext());

                final String pushkey = getRef(position).getKey();

                final DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

                mApprovedPaymentsDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            TotalPrice = TotalPrice + model.getAmount();
                            Balance = 200000 - TotalPrice;

                            tvTotalAmount.setText("Total Paid : " + decimalFormat.format(TotalPrice));

                            tvAmountBalance.setText("Balance : " + decimalFormat.format(Balance));

                            if (TotalPrice > 200000){
                                tvAmountBalance.setText("Fully Paid");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentDetailViewApprovedPayment = new Intent(ViewListOfApprovedPaymentsActivity.this, DetailViewPilgrimsApprovedActivity.class);
                        intentDetailViewApprovedPayment.putExtra("pushkey", pushkey);
                        intentDetailViewApprovedPayment.putExtra("user_id", user_id);
                        startActivity(intentDetailViewApprovedPayment);
                    }
                });
            }
        };

        viewListofApprovedPaymeentsRecyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    public static class viewListofApprovedPaymetnsViewholder extends RecyclerView.ViewHolder {

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

        View mView;

        public viewListofApprovedPaymetnsViewholder(View itemView) {

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
