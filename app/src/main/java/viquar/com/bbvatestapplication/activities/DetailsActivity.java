package viquar.com.bbvatestapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import viquar.com.bbvatestapplication.R;
import viquar.com.bbvatestapplication.model.BankLocation;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Button mDirectionsBtn;//Button that shows the directions from current location to the Bank
    TextView mNameView;//TextView to display the Name of the Bank
    TextView mLatLngView;//TextView to display the Latitude and Longitude values of the Bank
    TextView mAddressView;//TextView to display the Address of the Bank
    int mPosition;// Value to store the index of the marker
    double mLatitude,mLongitude;//the latitude and longitude of the Bank
    String mName;//the name of the selected Bank Marker
    ArrayList<BankLocation> bankLocationArrayList;//list of objects
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //initializing all the views
        mNameView = (TextView) findViewById(R.id.tv_name);
        mLatLngView = (TextView) findViewById(R.id.tv_latlng);
        mAddressView = (TextView) findViewById(R.id.tv_address);
        mDirectionsBtn = (Button) findViewById(R.id.btn_directions);
        mDirectionsBtn.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle recieved = intent.getExtras();
        mPosition=recieved.getInt("key",0);//getting the clicked position
        bankLocationArrayList=recieved.getParcelableArrayList("mapkey");
        displayDetails(mPosition);
    }
    public void displayDetails(int position) {
        mLatitude = bankLocationArrayList.get(position).getLatitude();
        mLongitude = bankLocationArrayList.get(position).getLongitude();
        mName = bankLocationArrayList.get(position).getName();
        mNameView.setText("Name: "+mName);
        mLatLngView.setText("Latitude: "+mLatitude+" Longitude: "+mLongitude);
        mAddressView.setText(bankLocationArrayList.get(position).getAddress());
    }

    @Override
    public void onClick(View view) {
        Uri mapUri = Uri.parse("google.navigation:q="+mLatitude+","+mLongitude+" ("+mName+")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
