package com.group395.ember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {

    //Specifies whether the app has just started
    private static boolean startUp = true;
    private static Movie[][] recentClicks = new Movie[5][8];
    //Specifies how many sets of 8 Movies have been moved past by the "next" button.
    private int pagesSkipped = 0;
   // private Logger logger = new Logger(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Logger.initializeContext(getApplicationContext());
        try {
            loadHistoryOnClick();
        } catch (ExecutionException e) {
            Log.e("Ember", e.getMessage());
        }
        if(startUp){
            Fresco.initialize(this);
        }

        displayAll();
        TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, pagesSkipped + 1));
        startUp = false;
    }

    public void searchButtonOnClick(View v){ startActivity(new Intent(HistoryActivity.this, SearchStartActivity.class)); }

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
        TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, pagesSkipped + 1));
    }
    public void nextOnClick(View v){
        if(pagesSkipped + 1 >= recentClicks.length){
            return;
        }
        pagesSkipped++;
        displayAll();
        TextView pageNumber = findViewById(R.id.pageNumber);
        pageNumber.setText(getApplicationContext().getString(R.string.page_number, pagesSkipped + 1));
    }

    private void tileOnClick(int whichButton){
        if(recentClicks[pagesSkipped][whichButton] != null) {
            Intent i = new Intent(HistoryActivity.this, MoviePageActivity.class);
            i.putExtra("title", recentClicks[pagesSkipped][whichButton].getTitle());
            i.putExtra("fromHistoryActivity", true);
            startActivity(i);
        }
    }

    public void loadHistoryOnClick() throws ExecutionException {
        Logger.initializeContext(getApplicationContext());

        try {

            // Movie titles only:
            FileInputStream inputStream = getApplicationContext().openFileInput(Logger.getMovieLog().getName());

            ArrayList<String> movieTitles = Logger.readByLine(inputStream);

            MovieLoader loader = new MovieLoader();
            loader.loadMoviesByTitle(movieTitles);

            if (movieTitles.size() > 40)
                Logger.trimCache(movieTitles.subList(1, 41));

            // Loop take to buttons
            for (int toSkip = 0; toSkip <= 5; toSkip++)
                for (int i = 1; i < Math.min(movieTitles.size() - toSkip * 8, 9); i++) {
                    System.out.println("i = " + (toSkip * 8 + i) + " pages = " + (toSkip) + " \n");
                    recentClicks[toSkip][i - 1] = loader.loadMovieByTitle(movieTitles.get(toSkip * 8 + i)).get();
                }
            displayAll();

        } catch (FileNotFoundException e) {

            Log.e("Ember", "File not found in loadhistory while pulling.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            firstEmptyArray = recentClicks.length - 1;
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
            }else {
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
    }

    private void display(Button button, Movie movie){
        if(movie == null){
            button.setText(null);
            return;
        }
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

    private void load() throws FileNotFoundException, InterruptedException {
        ArrayList<Movie> loadList = Logger.pullAllFromHistory();

        System.out.println(loadList.size());
        for (Movie m : loadList) {
            System.out.println(m);
        }

        //If there's no data in loadList, use the default size
        if(loadList.size() != 0){
            //How big the 2D array should be
            int maxRows = loadList.size() / 8;
            //Add an extra row if something got cut off by the int division
            if(maxRows * 8 != loadList.size()){ maxRows++; }
            Movie[][] tempFull = new Movie[maxRows][8];
            int outer = 0;
            int inner = 0;
            for(int i = 0; i < loadList.size(); i++) {
                tempFull[outer][inner] = loadList.get(i);
                if(inner == 7){
                    outer++;
                    inner = 0;
                }else {
                    inner++;
                }
            }
            recentClicks = tempFull;
        }
    }
}
