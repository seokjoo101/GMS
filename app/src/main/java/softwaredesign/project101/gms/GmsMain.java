package softwaredesign.project101.gms;

/**
 * Created by seokjoo on 2016-06-02.
 * UC3 - Manage Trainee Program
 * Main class ( Device_Handler)
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GmsMain extends Activity {
    Button AddWorkoutSchedule;
    Button CheckWorkoutSchedule;
    String add="add";
    String check="check";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gms_main);

        AddWorkoutSchedule= (Button)findViewById(R.id.manage);
        CheckWorkoutSchedule =(Button)findViewById(R.id.check);

        AddWorkoutSchedule.setOnClickListener(AddingWorkoutSchedule);
        CheckWorkoutSchedule.setOnClickListener(CheckingWorkoutSchedule);


    }


    // Move to adding WorkoutSchedule activity
    View.OnClickListener AddingWorkoutSchedule = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(GmsMain.this, Traineelist_DBhandler.class);
            intent.putExtra("AddOrChekck", add);
            startActivity(intent);
        }

    };

    // Move to checking WorkoutSchedule activity
    View.OnClickListener CheckingWorkoutSchedule = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent(GmsMain.this, Traineelist_DBhandler.class);
            intent.putExtra("AddOrChekck", check);
            startActivity(intent);
        }

    };
}
