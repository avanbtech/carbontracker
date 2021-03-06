package com.cmpt276.indigo.carbontracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;

import java.io.InputStream;
import java.util.Calendar;

/*
    implments main menu activity
 */
public class MainMenu extends Activity {
    public static final int JOURNEY_SELECT = 300;
    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        // process might be killed off while paused and the app is in the background.
        // In that case, the flag will be false when the activity is recreated by
        // the system when the user tries to bring the app back to the foreground/
        //use shared preference to get aroundt his
        loadDataFile();
        carbonFootprintSelectBtn();
        // add journey view button
        journeyViewBtn();
        utilitesCreateBtn();
        showNotification();

    }

    private void showNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 42);
        calendar.set(Calendar.SECOND, 00);
        Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);



    }


    //Go to carbon footprint activity
    private void carbonFootprintSelectBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_carbonfootprint_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, CarbonFootprintMainMenu.class);
                startActivity(intent);
            }
        });
    }

    private void loadDataFile() {
        CarbonFootprintComponentCollection carbonFootprint = CarbonFootprintComponentCollection.getInstance();
        InputStream is = getResources().openRawResource(R.raw.vehicles);
        carbonFootprint.loadDataFile(is);
    }

    /**
     * jump to JourneySelectActivity
     */
    private void journeyViewBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_view_journey_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, JourneySelectActivity.class);
                startActivityForResult(intent,23 );
            }
        });
    }

    //Launch Create a utitliy activity
    private void utilitesCreateBtn() {
        Button btn = (Button) findViewById(R.id.main_menu_create_utilities_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, UtilitySelectActivity.class);
                //startActivityForResult(intent, JOURNEY_SELECT );
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        }
    }

}



