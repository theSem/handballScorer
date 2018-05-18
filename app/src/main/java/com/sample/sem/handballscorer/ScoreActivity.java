package com.sample.sem.handballscorer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    private int mScore1;
    private int mScore2;
    private int mServer;

    private TextView mScoreView1;
    private TextView mScoreView2;

    static final String SCORE_1 = "Team 1 Score";
    static final String SCORE_2 = "Team 2 Score";
    static final String SERVER = "Current Server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Define the score views
        mScoreView1 = findViewById(R.id.score1);
        mScoreView2 = findViewById(R.id.score2);

        //Check if there is saved data and update if there is
        if (savedInstanceState != null){
            mScore1 = savedInstanceState.getInt(SCORE_1);
            mScore2 = savedInstanceState.getInt(SCORE_2);
            mServer = savedInstanceState.getInt(SERVER);
            updateGame();

            //Default server is 0 so if saved is 1, rotate servers
            if (mServer == 1) {
                switchServer();
            }
        }

        Button mPoint1 = findViewById(R.id.point1);
        Button mPoint2 = findViewById(R.id.point2);


        //Onclick listener for both the point buttons
        View.OnClickListener inc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementScore(view);
            }
        };

        mPoint1.setOnClickListener(inc);

        mPoint2.setOnClickListener(inc);
    }

    private void incrementScore(View view){
        int viewID = view.getId();
        switch (viewID){
            case R.id.point1:
                if (mServer == 0) {
                    mScore1++;
                } else {
                    switchServer();
                }
                break;
            case R.id.point2:
                if (mServer == 1) {
                    mScore2++;
                } else {
                    switchServer();
                }
        }
        if (mScore1 == 21 || mScore2 == 21) {
            Toast.makeText(this,"Player " + (mServer + 1) + " wins!", Toast.LENGTH_LONG).show();
            resetGame();
        }
        updateGame();
    }

    private void resetGame(){
        mScore1 = 0;
        mScore2 = 0;
        mServer = 1;
        switchServer();
    }

    private void updateGame(){

        //Update the scores
        mScoreView1.setText(String.valueOf(mScore1));
        mScoreView2.setText(String.valueOf(mScore2));
        }

    private void switchServer(){
        //Change the server
        mServer = (mServer + 1) % 2;

        //Find the width of the screen
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;

        //Grab the slider view
        View sliderView = findViewById(R.id.slider);

        //Animate the server bar
        sliderView.animate().translationX(dpWidth*mServer/2);

        //Change the text in the point buttons
        Button point1 = findViewById(R.id.point1);
        Button point2 = findViewById(R.id.point2);

        if (mServer == 0){
            point1.setText(R.string.increment);
            point2.setText(R.string.side_out);
        } else {
            point1.setText(R.string.side_out);
            point2.setText(R.string.increment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the current score and server
        outState.putInt(SCORE_1, mScore1);
        outState.putInt(SCORE_2, mScore2);
        outState.putInt(SERVER, mServer);
        super.onSaveInstanceState(outState);
    }
}
