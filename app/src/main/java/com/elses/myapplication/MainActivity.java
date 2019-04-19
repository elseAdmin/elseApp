package com.elses.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Date;
import android.widget.Toast;

import com.elses.service.MetricsService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Else Project";
    private static final int PERMISSIONS_REQUEST_CODE = 1111;
    private MessageListener mMessageListener;
    private Message mMessage;
    private OnFailureListener onFailureListener;
    private DatabaseReference myRef;
    MetricsService metricsService ;
    Button startRecording,stopRecording;
    EditText distance1,distance2,distance3,distance4,testCase;
    Date startedAt,stoppedAt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myRef = FirebaseDatabase.getInstance().getReference();

        startRecording = (Button)findViewById(R.id.start);
        stopRecording = (Button)findViewById(R.id.stop);
        distance1 = (EditText)findViewById(R.id.distance1);
        distance2 = (EditText)findViewById(R.id.distance2);
        distance3 = (EditText)findViewById(R.id.distance3);
        distance4 = (EditText)findViewById(R.id.distance4);
        testCase = (EditText)findViewById(R.id.testCase);

        startRecording.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Started recording beacon metrics.", Toast.LENGTH_LONG);
                Constants.recording=true;
                startedAt = new Date();
                try {
                    Constants.distance1 = new Double(distance1.getText().toString());
                    Constants.distance2 = new Double(distance2.getText().toString());
                    Constants.distance3 = new Double(distance3.getText().toString());
                    Constants.distance4 = new Double(distance4.getText().toString());
                    Constants.testCase = testCase.getText().toString();
                }catch(NumberFormatException e){
                    toast = Toast.makeText(context, "Input valid numbers.", Toast.LENGTH_LONG);
                }finally {
                    Log.i(TAG, Double.toString(Constants.distance1));
                    toast.show();
                }
            }
        });

        stopRecording.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Recording stopped.", Toast.LENGTH_LONG);
                Constants.recording=false;
                stoppedAt=new Date();
                metricsService.pushMetaData(myRef,startedAt,stoppedAt);
                toast.show();
            }
        });

        if (!havePermissions()) {
            Log.i(TAG, "Requesting permissions needed for this app.");
            requestPermissions();
        }
        onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG,"failed");
            }
        };

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i("nameSpace : ",message.getNamespace());
                Log.i(TAG, "Found message: " + new String(message.getContent()));
                Log.i(TAG, "Namespace : " + new String(message.getNamespace()));
                Log.i(TAG, "Content : " + new String(message.getContent()));
                Log.i(TAG, "Type : " + new String(message.getType()));
                Log.i(TAG, "Timestamp : " + new String(String.valueOf(message.hashCode())));
            }

            @Override
            public void onLost(Message message) {
                System.out.print(message.getNamespace());
                Log.i(TAG, "Lost sight of message: " + new String(message.getContent()));
            }

            /**
             * Called when the Bluetooth Low Energy (BLE) signal associated with a message changes.
             *
             * This is currently only called for BLE beacon messages.
             *
             * For example, this is called when we see the first BLE advertisement
             * frame associated with a message; or when we see subsequent frames with
             * significantly different received signal strength indicator (RSSI)
             * readings.
             *
             * For more information, see the MessageListener Javadocs.
             */
            @Override
            public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                super.onBleSignalChanged(message, bleSignal);

                metricsService = new MetricsService();
                metricsService.pushMetrics(myRef,message,bleSignal);

            }

            /**
             * Called when Nearby's estimate of the distance to a message changes.
             *
             * This is currently only called for BLE beacon messages.
             *
             * For more information, see the MessageListener Javadocs.
             */
          /*  @Override
            public void onDistanceChanged(final Message message, final Distance distance) {
                Log.i(TAG, "Message " + new String(message.getContent()) + " Distance changed, new distance: " + distance);
            }*/
        };

        mMessage = new Message("Hello World".getBytes());
    }
    private void subscribe() {
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.DEFAULT)
                // Note: If no filter is specified, Nearby will return all of your
                // attachments regardless of type. You must use a filter to specify
                // a particular set of attachments (by type) or to fetch attachments
                // in a namespace other than your project's default.
                .build();

        Nearby.getMessagesClient(this).subscribe(mMessageListener).addOnFailureListener(onFailureListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (havePermissions()) {
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "Connection suspended. Error code: " + i);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "GoogleApiClient connected");
        subscribe();
    }
    private synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Nearby.MESSAGES_API, new MessagesOptions.Builder()
                            .setPermissions(NearbyPermissions.BLE).build())
                    .addConnectionCallbacks(this)
                    .enableAutoManage(this, this)
                    .build();
        }
    }
    private boolean havePermissions() {
        return ContextCompat.checkSelfPermission(this,     Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
    }
}
