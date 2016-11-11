package farrowc.gpsrunner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import farrowc.gpsrunner.HighScore.HighScores;
import farrowc.gpsrunner.HighScore.HighScoresDataSource;

public class LocationReachedActivity extends AppCompatActivity {

    private HighScoresDataSource highScoresDataSource;
    private HighScores highScores;

    long timeElapsed;
    double distanceTravelled;
    double minimumDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_reached);
        initializeDB();
        if (getIntent().getExtras() != null) {
            minimumDistance = getIntent().getExtras().getDouble("MinimumDistance");
            distanceTravelled = getIntent().getExtras().getDouble("DistanceTravelled");
            timeElapsed = getIntent().getExtras().getLong("Time");
        }

        if(highScoresDataSource.getAllHighScores().size()>0){
            highScores = highScoresDataSource.getAllHighScores().get(0);
        }else{
            highScoresDataSource.createHighScore(timeElapsed,distanceTravelled);
        }
        if(distanceTravelled>highScores.getDistance()||
                (distanceTravelled==highScores.getDistance()&&timeElapsed<highScores.getTime())){
            highScores.setDistance(distanceTravelled);
            highScores.setTime(timeElapsed);
            highScoresDataSource.updateHighScore(highScores);
        }

        ((TextView)findViewById(R.id.timeElapsed)).setText("Time Taken: "+timeElapsed/1000+" s");
        ((TextView)findViewById(R.id.distanceTravelled)).setText("Distance Travelled: "+distanceTravelled+" ft");
        ((TextView)findViewById(R.id.minimumDistance)).setText("Minimum Distance: "+minimumDistance+" ft");
        ((TextView)findViewById(R.id.highDistance)).setText("Highest Distance Travelled: "+highScores.getDistance()+" ft");
        ((TextView)findViewById(R.id.highTime)).setText("Time of Highest Distance: "+highScores.getTime()+" s");
    }

    private boolean initializeDB(){
        int positiveDBConnections = 0;
        highScoresDataSource = new HighScoresDataSource(this);
        if(highScoresDataSource.open()){
            positiveDBConnections++;
        }
//        datasource = new ItemDataSource(this);
//        datasource.open();
        return (positiveDBConnections == 1);
    }

    public void backToMap(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
