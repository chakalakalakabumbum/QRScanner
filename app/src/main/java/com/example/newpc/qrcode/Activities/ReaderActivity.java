package com.example.newpc.qrcode.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newpc.qrcode.Entities.Booking;
import com.example.newpc.qrcode.Entities.ResponseTemplate;
import com.example.newpc.qrcode.R;
import com.example.newpc.qrcode.Retrofit.APIClient;
import com.example.newpc.qrcode.Retrofit.APIInterface;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private TextView lastResult;
    private TextView bookingStatus;
    private TextView bookedCarPark;
    private TextView moneyToPay;
    private ImageView blackScreen;
    private Booking booking;

    //API
    private APIInterface apiInterface;
    private APIClient apiClient;

    //URL
    String customURL;
    String fixedURL;
    boolean haveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        bindView();
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    private void bindView(){
        customURL = "http://" + getIntent().getSerializableExtra("CUSTOM_URL") + "/";
        lastResult = ReaderActivity.this.findViewById(R.id.last_result);
        bookingStatus = ReaderActivity.this.findViewById(R.id.book_status);
        bookedCarPark = ReaderActivity.this.findViewById(R.id.booked_carpark);
        moneyToPay = ReaderActivity.this.findViewById(R.id.money_to_pay);

        blackScreen = ReaderActivity.this.findViewById(R.id.screen_loading);
        if(customURL.isEmpty()){
            customURL = getResources().getString(R.string.main_link);
        }
        apiInterface = APIClient.getClient(customURL).create(APIInterface.class);
        apiClient = new APIClient();
    }

    public void preventClick() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void resumeClick() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    List<NameValuePair> params = URLEncodedUtils.parse(new URI(result.getContents()), Charset.forName("UTF-8").displayName());
                    haveId = false;
                    for (final NameValuePair param : params) {
                       if(param.getName().equals("bookingId")){
                           haveId = true;
                           blackScreen.setVisibility(View.VISIBLE);
                           preventClick();
                           try {
                               Log.d("QR", param.getValue());
                               apiInterface.dogetBooking(Integer.parseInt(param.getValue())).enqueue(new Callback<ResponseTemplate>() {
                                   @Override
                                   public void onResponse(Call<ResponseTemplate> call, Response<ResponseTemplate> response) {
                                       if (response.body() == null) {
                                           Toast.makeText(ReaderActivity.this, "Invalid QR code", Toast.LENGTH_LONG).show();
                                       } else if (response.body().getObjectResponse() == null) {
                                           Toast.makeText(ReaderActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                       } else {
                                           booking = (Booking) apiClient.ObjectConverter(response.body().getObjectResponse(), new Booking());
                                           Toast.makeText(ReaderActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                           AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReaderActivity.this);
                                           alertDialog.setTitle("The plate number is: " + booking.getPlateNumber());
                                           alertDialog.setMessage("Supervisor should check for driver license plate number \n" +
                                                   "if nothing is changed, this will be default");
                                           //alertDialog.setView();
                                           alertDialog.setPositiveButton("Ok, this is the right number", new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog,int which) {
                                                   Log.d("QR", (customURL + result.getContents().replace("http://localhost:8080/", "")));
                                                   blackScreen.setVisibility(View.VISIBLE);
                                                   preventClick();
                                                   apiInterface.doCheck(fixedURL).enqueue(new Callback<ResponseTemplate>() {
                                                       @Override
                                                       public void onResponse(Call<ResponseTemplate> call, Response<ResponseTemplate> response) {
                                                           Log.d("QRX", response.message());
                                                           lastResult.setText(response.message());
                                                           if (response.body() == null) {
                                                               Toast.makeText(ReaderActivity.this, "Invalid QR code", Toast.LENGTH_LONG).show();
                                                           }
                                                           else if (response.body().getObjectResponse() == null) {
                                                               Toast.makeText(ReaderActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                                           } else {
                                                               booking = (Booking) apiClient.ObjectConverter(response.body().getObjectResponse(), new Booking());
                                                               Toast.makeText(ReaderActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                                               bookingStatus.setText("Booking status: " + (booking.getBookingStatus() != null? booking.getBookingStatus() : "N/A"));
                                                               bookedCarPark.setText("Booked carpark: " + (booking.getParkingLotName() != null? booking.getParkingLotName() : "N/A"));
                                                               moneyToPay.setText("Money to pay: " + (booking.getMoneyToPay()  != 0? String.format("%.0f", booking.getMoneyToPay()) : "N/A"));
                                                           }
                                                           blackScreen.setVisibility(View.INVISIBLE);
                                                           resumeClick();
                                                       }

                                                       @Override
                                                       public void onFailure(Call<ResponseTemplate> call, Throwable t) {
                                                           String displayResponse = t.toString();
                                                           Log.d("TAG", displayResponse);
                                                           Toast.makeText(ReaderActivity.this, "Unable to connect to the server", Toast.LENGTH_LONG).show();
                                                           blackScreen.setVisibility(View.INVISIBLE);
                                                           resumeClick();
                                                       }
                                                   });

                                               }
                                           });
                                           // on pressing cancel button
                                           alertDialog.setNegativeButton("Deny, this is not the right number", new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int which) {
                                                   dialog.cancel();
                                                   Toast.makeText(ReaderActivity.this, "Check denied", Toast.LENGTH_LONG).show();
                                               }
                                           });

                                           // Showing Alert Message
                                           alertDialog.show();
                                           if(haveId == false){
                                               Toast.makeText(ReaderActivity.this, "Invalid QR code", Toast.LENGTH_LONG).show();
                                           }

                                       }
                                       blackScreen.setVisibility(View.INVISIBLE);
                                       resumeClick();
                                   }

                                   @Override
                                   public void onFailure(Call<ResponseTemplate> call, Throwable t) {
                                       String displayResponse = t.toString();
                                       Log.d("TAG", displayResponse);
                                       Toast.makeText(ReaderActivity.this, "Unable to connect to the server", Toast.LENGTH_LONG).show();
                                       blackScreen.setVisibility(View.INVISIBLE);
                                       resumeClick();
                                   }

                               });
                           }
                           catch(Exception e){
                               Log.d("QR", e.toString());
                               Toast.makeText(ReaderActivity.this, "Invalid QR code", Toast.LENGTH_LONG).show();
                           }
                       }
                    }
                }
                catch(Exception e){
                    Log.d("QR", e.toString());
                    Toast.makeText(ReaderActivity.this, "Invalid QR code", Toast.LENGTH_LONG).show();
                }

                try {
                    fixedURL = result.getContents().replace("http://localhost:8080/", "");
                }
                catch (Exception e){
                    fixedURL = "";
                }






            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
