package softwaredesign.project101.gms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by seokjoo on 2016-06-03.
 * Show trainee's workoutschedule from workout table of mysqlDB
 */
public class ShowWorkoutSchedule extends Activity {


    TextView view;
    int scrollamout;
    String list = "";
    int traineeId;


    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_workoutschedule);

        Intent intent = getIntent();
        traineeId = intent.getIntExtra("traineeId",1)+1;

        //Select workoutSchedule date button
        findViewById(R.id.LoaddateButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("seok",""+traineeId);
                new DatePickerDialog(ShowWorkoutSchedule.this, dateSetListener, year, month, day).show();
            }
        });






        GregorianCalendar calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day= calendar.get(Calendar.DAY_OF_MONTH);

        view = (TextView) findViewById(R.id.showWorkout);
        view.setMaxLines(100);
        view.setVerticalScrollBarEnabled(true);
        view.setMovementMethod(new ScrollingMovementMethod());


    }

    //establish workout date
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            month=monthOfYear+1;
            day=dayOfMonth;
            String msg = String.format("Load %d / %d / %d", year,monthOfYear+1, dayOfMonth);
            Toast.makeText(ShowWorkoutSchedule.this, msg, Toast.LENGTH_SHORT).show();
            new Thread(loads).start();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gms_main, menu);
        return true;
    }

    //Access server and open workoutschedule db
    Runnable loads = new Runnable() {
        public void run() {
            InputStream is = null;
            try {
                HttpResponse resp = new DefaultHttpClient().execute(new HttpGet("http://192.168.43.36/showWorkoutSchedule.php?request=show&traineeid=" + traineeId + "&year=" +
                        + year + "&month=" + month + "&day=" + day ));
                is = resp.getEntity().getContent();
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            if (is != null) {
                readStream(is);
            }
        }

        //read trainee workoutSchedule on db
        private void readStream(InputStream is) {
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(isReader);
            String line = "";
            try {
                while ((line = rd.readLine()) != null) {
                    list += ("\n" + line);
                }
                runOnUiThread(settext);
            } catch (IOException e1) {
                Log.e("error", "error");
            }
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    };

    //renew listview
    Runnable settext = new Runnable() {

        public void run() {

            view.setText(list);

            scrollamout = view.getLayout().getLineTop(view.getLineCount()) - view.getHeight();
            if (scrollamout > view.getHeight()) {
                view.scrollTo(0, scrollamout);
            }
        }
    };


}
