package com.group395.ember;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HistoryActivity extends AppCompatActivity {

    //TODO: Replace Movie[] with Stack for a FILO display (currently FIFO)
    private static Movie[] recentClicks = new Movie[8];
    //This field specifies how many Movies have been moved past by the "next" button.
    private int displacement = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        displayAll();
    }

    public void searchButtonOnClick(View v){ startActivity(new Intent(HistoryActivity.this, SearchOptionsActivity.class)); }

    public void tileALOnClick(View v) { tileOnClick(0); }
    public void tileAROnClick(View v) { tileOnClick(1); }
    public void tileBLOnClick(View v) { tileOnClick(2); }
    public void tileBROnClick(View v) { tileOnClick(3); }
    public void tileCLOnClick(View v) { tileOnClick(4); }
    public void tileCROnClick(View v) { tileOnClick(5); }
    public void tileDLOnClick(View v) { tileOnClick(6); }
    public void tileDROnClick(View v) { tileOnClick(7); }

    public void previousOnClick(View v){
        if(displacement >= 8) {
            displacement -= 8;
            displayAll();
        }
    }
    public void nextOnClick(View v){
        if(recentClicks.length <= displacement + 8){
            return;
        }
        displacement += 8;
        displayAll();
    }

    private void tileOnClick(int whichButton){
        MoviePageActivity.setCurrentMovie(recentClicks[whichButton]);
        startActivity(new Intent(HistoryActivity.this, MoviePageActivity.class));
    }

    protected static void addClick(Movie clickedMovie){
        int firstEmpty = SearchOptionsActivity.findFirstEmpty(recentClicks);
        if(firstEmpty == -1){
            Movie[] tempClicks = new Movie[recentClicks.length + 8];
            for(int i = 0; i < recentClicks.length; i++){
                tempClicks[i] = recentClicks[i];
            }
            recentClicks = tempClicks;
        }
        recentClicks[firstEmpty] = clickedMovie;
    }

    private void display(Button button, Movie movie){
        if(movie == null) return;
        button.setText(movie.getTitle());
    }
    private void displayAll(){
        display((Button) findViewById(R.id.tileAL), recentClicks[displacement]);
        display((Button) findViewById(R.id.tileAR), recentClicks[1 + displacement]);
        display((Button) findViewById(R.id.tileBL), recentClicks[2 + displacement]);
        display((Button) findViewById(R.id.tileBR), recentClicks[3 + displacement]);
        display((Button) findViewById(R.id.tileCL), recentClicks[4 + displacement]);
        display((Button) findViewById(R.id.tileCR), recentClicks[5 + displacement]);
        display((Button) findViewById(R.id.tileDL), recentClicks[6 + displacement]);
        display((Button) findViewById(R.id.tileDR), recentClicks[7 + displacement]);
    }

}
