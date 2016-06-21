package softwaredesign.project101.gms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by seokjoo on 2016-06-03.
 * Accesing TraineeList DB classs
 */

public class Traineelist_DBhandler extends Activity {
    String AddOrChekck;
    String[] names;
    int tId;
    ListView list;
    Bitmap bm;
    URL url;

    ArrayList<String> traineeName= new ArrayList<String>();
    ArrayList<String> traineesImage= new ArrayList<String>();
    ArrayList<Bitmap> image = new ArrayList<Bitmap>();
    ImageView traineeface;


    NewArrayAdapter CustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainee_list);

        list =(ListView)findViewById(R.id.list);
        traineeface = (ImageView)findViewById(R.id.photo);

        Intent intent = getIntent();
        AddOrChekck = intent.getStringExtra("AddOrChekck");

        CustomAdapter = new NewArrayAdapter(this);

        new Thread(loads).start();

        list.setOnItemClickListener(new ListViewItemClickListener());

    }

    //Select making wokroutSchedule activity or showing workoutSchedule activity
    class ListViewItemClickListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            Log.i("seok",""+position);

            tId=position;

            if (AddOrChekck.equals("add")) {

                Intent intent = new Intent(Traineelist_DBhandler.this, MakeWorkoutSchedule.class);
                intent.putExtra("traineeId", tId);
                //send id
                //move to make workoutschedule activity
                startActivity(intent);


            } else if (AddOrChekck.equals("check")) {

                Intent intent = new Intent(Traineelist_DBhandler.this, ShowWorkoutSchedule.class);
                intent.putExtra("traineeId", tId);
                //send id
                //move to check workoutschedule activity
                startActivity(intent);

            }
        }


    }


    //Access server and open tranelisst db
    Runnable loads = new Runnable() {
        public void run() {
            InputStream is = null;
            try {
                HttpResponse resp = new DefaultHttpClient().execute(new HttpGet("http://192.168.43.36/selectTrainee.php"));
                is = resp.getEntity().getContent();
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            if (is != null) {
                readStream(is);
            }
        }

        //read trainee image and trainee name on db
        private void readStream(InputStream is) {
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(isReader);
            String line = "";
            try {
                while ((line = rd.readLine()) != null) {

                    names = line.split(",");

                    traineeName.add(names[0]);
                    String urlstring= "http://192.168.43.36/"+names[1];
                    traineesImage.add(urlstring);

                    url = new URL(urlstring);
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    bm = BitmapFactory.decodeStream(bis);
                    image.add(bm);
                    bis.close();


                }

                for(int i=0;i<traineeName.size();i++){
                    Log.i("seok",traineeName.get(i));
                }
                for(int i=0;i<traineesImage.size();i++){
                    Log.i("seok",traineesImage.get(i));
                }
                runOnUiThread(setListview);

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
    Runnable setListview = new Runnable() {

        public void run() {
            list.setAdapter(CustomAdapter);
            CustomAdapter.notifyDataSetChanged();

        }
    };



    //ListView Adatper to show traineeList in DB
    class NewArrayAdapter extends ArrayAdapter {
        Activity context;

        NewArrayAdapter(Activity context ) {
            super(context, R.layout.trainee_list,traineeName);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View row = inflater.inflate(R.layout.trainee_item, null);
            ImageView i1 = (ImageView) row.findViewById(R.id.photo);

            TextView textView = (TextView) row.findViewById(R.id.name);
            textView.setText(traineeName.get(position));
            i1.setImageBitmap(image.get(position));



            return row;
        }
    }




}
