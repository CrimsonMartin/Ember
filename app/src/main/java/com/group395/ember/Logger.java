
package com.group395.ember;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Logger {

    private static Context context;
    private static File cache;
    // For general errors and other data
    private static File generalLog = new File(cache, "logfile.txt");
    // For just movie history
    private static File movieLog = new File(cache, "movies.txt");

    /**
     * Default constructor; takes context (ctx) to create cache files for logging.
     * @param ctx is Context to use for logging.
     */
    protected Logger(Context ctx) {
        context = ctx;

        if (!context.getCacheDir().exists())
            context.getCacheDir().mkdirs();

        cache = context.getCacheDir();
    }

    protected static void initializeContext(Context ctx) {
        context = ctx;

        if (!context.getCacheDir().exists())
            context.getCacheDir().mkdirs();

        cache = context.getCacheDir();
    }

    protected static File getGeneralLog() { return generalLog; }

    protected static File getMovieLog() { return movieLog; }

    protected static void setMovieLog(File newLog) { movieLog = newLog; }

    protected static String[] getFileList() { return context.fileList(); }

    /**
     * Logs an exception related to any of the other classes. Logs are available using `adb logcat` on phone in dev mode.
     * @param e is the exception to log
     */
    protected static void logException(Exception e) {
        Log.e("Ember", e.getMessage());
        e.printStackTrace();
    }

    /**
     * Writes any String to this log
     * @param s is what to write
     */
    protected static void write(String s) { Log.d("Ember", s); }

    /**
     * Saves a movie to the movie file in the cache.
     * @param movie to save to history
     */
    protected static void saveToHistory(Movie movie) {
        try {
            // TODO: getTitle() -> getImdbID()
//        if (this.write(movie.getImdbID()))
            // Temp identification

            FileOutputStream outputStream = context.openFileOutput(getMovieLog().getName(), Context.MODE_PRIVATE | Context.MODE_APPEND);
            outputStream.write((movie.getTitle() + "\n").getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e("Ember", e.getMessage());
            logException(new Exception(movie.getTitle() + " Failed to store write."));
            System.out.println(e.toString());
        }
    }

    protected static void saveToHistory(String title) {
        try {
            FileOutputStream outputStream = context.openFileOutput(getMovieLog().getName(), Context.MODE_PRIVATE | Context.MODE_APPEND);
            outputStream.write((title + "\n").getBytes());
            outputStream.close();
        } catch(Exception e) {
            Log.e("Ember", e.getMessage());
            logException(new Exception(title + " Failed to store write."));
            System.out.println(e.toString());
        }
    }

    /**
     * Reads the movie history file and turns them into an ArrayList
     * @return ArrayList of Movies
     */
    protected static ArrayList<Movie> pullAllFromHistory() throws FileNotFoundException, InterruptedException {

        FileInputStream inputStream = context.openFileInput(getMovieLog().getName());

        ArrayList<String> movieIDs = readByLine(inputStream);
        ArrayList<Movie> movies = new ArrayList<>();
        UISearch tempSearch = new UISearch();

        if (movieIDs.size() != 0) {
            // Gets each movie from the DB again
            for (String title : movieIDs) {
                Log.e("Ember", "Success  for @hist");
                // TODO: getTitle() -> getImdbID()
                tempSearch.setSearch(title);
                List<Movie> ms = tempSearch.search(6);

                movies.add(ms.get(0));
                Log.e("Ember", ms.get(0).getTitle());
            }
        }

        return movies;
    }


    /**
     * Reads the targeted FileInputStream line by line and returns an ArrayList of each line as a String
     * @param inputStream is the file to read
     * @return ArrayList of Strings
     */
    protected static ArrayList<String> readByLine(FileInputStream inputStream) {
        try {
            ArrayList<String> output = new ArrayList<>();
            Scanner toRead = new Scanner(inputStream);
            toRead.useDelimiter("\n");
            while (toRead.hasNextLine()) {
                output.add(toRead.nextLine());
            }

            return output;
        }
        catch (Exception e) {
            Log.e("Ember", "Failed in reading " + inputStream);
            return null;
        }
    }

    /**
     * Trims the cache to only contain the List of Strings provided.
     * @param toWrite is the List to keep.
     */
    protected static void trimCache(List<String> toWrite) {
        context.deleteFile(getMovieLog().getName());
        setMovieLog(new File(cache, "movies.txt"));
        // If the size of the last reading is greater than the limit, clean up the cache by trimming the list.
        for (int i = toWrite.size(); i > 0; i--) {
            // Need to delete it first...
            saveToHistory(toWrite.get(toWrite.size() - i));
            Log.e("Ember", "Readded" + toWrite.get(toWrite.size() - i));
        }
    }



}
