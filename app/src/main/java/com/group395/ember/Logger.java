
package com.group395.ember;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;


public class Logger {

    // For general errors and other data
    private String fileName;
    // For just movie history
    private String movieFileName;


    /**
     * Default constructor; uses a default file name: EmberLogDDMMYY
     */
    public Logger() {
        // Formatting date
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dMMMyy");
        fileName = "EmberLog" + date.format(dateFormat) + ".txt";
        movieFileName = "MovieLog" + date.format(dateFormat) + ".txt";
    }

    /**
     * Constructor for a Logger, takes a String for a file name
     * @param fileName is a file to write logging details to
     */
    public Logger(String fileName) {
        this.fileName = fileName + ".txt";
    }


    /**
     * Logs an exception related to any of the other classes.
     * @param e is the exception to log
     */
    public void logException(Exception e) throws IOException {
        FileWriter file = new FileWriter(fileName, true);
        // Writing relevant data pertaining to the Exception
        file.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m")));
        file.write(e.getMessage());
        file.write(e.getCause().toString());
        file.write(e.getStackTrace().toString() + "\n");

        file.close();
    }

    /**
     * Saves a movie to the movie file of the day.
     * @param movie to save to history
     */
    public void saveToHistory(Movie movie) throws IOException {
        // TODO: getTitle() -> getImdbID()
//        if (this.write(movie.getImdbID()))
        // Temp identification
        if (!write(movie.getTitle(), movieFileName)) {
            logException(new Exception(movie.getTitle() + " Failed to store write."));
        }
    }


    /**
     * Reads the movie history file and turns them into an ArrayList
     * @return ArrayList of Movies
     */
    public ArrayList<Movie> pullAllFromHistory() {
        ArrayList<String> movieIDs = readByLine(movieFileName);
        ArrayList<Movie> movies = new ArrayList<Movie>();
        UISearch tempSearch = new UISearch();

        // Gets each movie from the DB again
        for (String title : movieIDs) {
            // TODO: getTitle() -> getImdbID()
            tempSearch.setSearch(title);
            movies.add(tempSearch.search().get(0));
        }

        return movies;
    }

    /**
     * Writes any String to this log
     * @param s is what to write
     * @return true if write was successful, false if exception thrown
     */
    public boolean write(String s, String fileName) {
        try {
            FileWriter file = new FileWriter(fileName, true);
            file.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m")));
            file.write(s + "\n");
            file.close();
            return true;
        }
        catch (Exception e) {
            System.out.println("There was an error writing to a file");
            return false;
        }
    }


    /**
     * Reads the targeted file line by line and returns an ArrayList of each line as a String
     * @param fileName is the file to read
     * @return ArrayList of Strings
     */
    public ArrayList<String> readByLine(String fileName) {
        try {
            ArrayList<String> output = new ArrayList<String>();
            Scanner toRead = new Scanner(new File(fileName));
            while (toRead.hasNextLine()) {
                output.add(toRead.nextLine());
            }

            return output;
        }
        catch (Exception e) {
            System.out.println("Failed in reading " + fileName);
            return null;
        }
    }


}
