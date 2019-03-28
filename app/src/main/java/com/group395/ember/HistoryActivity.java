package com.group395.ember;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Stack;

public class HistoryActivity extends AppCompatActivity {


    private static Movie[][] recentClicks = new Movie[5][8];
    //This field specifies how many sets of 8 Movies have been moved past by the "next" button.
    private int pagesSkipped = 0;

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
        if(pagesSkipped > 0) {
            pagesSkipped --;
            displayAll();
        }
    }
    public void nextOnClick(View v){
        if(pagesSkipped + 1 >= recentClicks.length){
            return;
        }
        pagesSkipped ++;
        displayAll();
    }

    private void tileOnClick(int whichButton){
        MoviePageActivity.setCurrentMovie(recentClicks[pagesSkipped][whichButton]);
        startActivity(new Intent(HistoryActivity.this, MoviePageActivity.class));
    }

    protected static void addClick(Movie clickedMovie) {
        int firstEmptyArray = -1;
        int firstEmptyIndex = -1;
        //Find first empty spot in recentClicks, designated [firstEmptyArray][firstEmptyIndex].
        for (int i = 0; i < recentClicks.length; i++) {
            if (firstEmptyArray == -1) {
                firstEmptyIndex = SearchOptionsActivity.findFirstEmpty(recentClicks[i]);
                if (firstEmptyIndex != -1) {
                    firstEmptyArray = i;
                }
            }
        }
        //If the entire array is full, expand it by one row.
        if (firstEmptyArray == -1) {
            Movie[][] tempRecentClicks = new Movie[recentClicks.length + 1][8];
            for(int i = 0; i < recentClicks.length; i++) {
                for(int j = 0; j < 8; j++) {
                    tempRecentClicks[i][j] = recentClicks[i][j];
                }
            }
            recentClicks = tempRecentClicks;
        }
        //If the entire array is empty, insert clickedMovie into the first slot.
        if ((firstEmptyArray == 0) && (firstEmptyIndex == 0)) {
            recentClicks[0][0] = clickedMovie;
            return;
        }
        for (int i = firstEmptyArray; i >= 0; i--) {
            //If all subarrays are either full or empty, copy the last entry in the last row to a new row before proceeding.
            if (firstEmptyIndex == 0) {
                recentClicks[firstEmptyArray][firstEmptyIndex] = recentClicks[firstEmptyArray - 1][7];
            }
            for (int j = 7; j > 0; j--) {
                recentClicks[i][j] = recentClicks[i][j - 1];
            }
            if (i > 0) {
                recentClicks[i][0] = recentClicks[i - 1][7];
            } else {
                recentClicks[0][0] = clickedMovie;
            }
        }
    }

    private void display(Button button, Movie movie){
        if(movie == null) return;
        button.setText(movie.getTitle());
    }
    private void displayAll(){
        display((Button) findViewById(R.id.tileAL), recentClicks[pagesSkipped][0]);
        display((Button) findViewById(R.id.tileAR), recentClicks[pagesSkipped][1]);
        display((Button) findViewById(R.id.tileBL), recentClicks[pagesSkipped][2]);
        display((Button) findViewById(R.id.tileBR), recentClicks[pagesSkipped][3]);
        display((Button) findViewById(R.id.tileCL), recentClicks[pagesSkipped][4]);
        display((Button) findViewById(R.id.tileCR), recentClicks[pagesSkipped][5]);
        display((Button) findViewById(R.id.tileDL), recentClicks[pagesSkipped][6]);
        display((Button) findViewById(R.id.tileDR), recentClicks[pagesSkipped][7]);
    }

}
