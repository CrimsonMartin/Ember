
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
    private static String fileName;
    // For just movie history
    private static String movieFileName;

    /**
     * Default constructor; uses a default file name: EmberLogDDMMYY
     */
    protected Logger() throws IOException {
        // Formatting date
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dMMMyy");
        fileName = "EmberLog" + date.format(dateFormat) + ".txt";
        movieFileName = "MovieLog" + date.format(dateFormat) + ".txt";

        File generalFile = new File(fileName);
        generalFile.createNewFile();

        File movieFile = new File(movieFileName);
        movieFile.createNewFile();

    }

    /**
     * Constructor for a Logger, takes a String for a file name
     * @param fileName is a file to write logging details to
     */
    protected Logger(String fileName) {
        this.fileName = fileName + ".txt";
    }

    protected static String getFileName() { return fileName; }

    protected static String getMovieFileName() { return fileName; }

    /**
     * Logs an exception related to any of the other classes.
     * @param e is the exception to log
     */
    protected static void logException(Exception e) throws IOException {

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
    protected static void saveToHistory(Movie movie) throws IOException {
        try {
            // TODO: getTitle() -> getImdbID()
//        if (this.write(movie.getImdbID()))
            // Temp identification
            write(movie.getTitle(), movieFileName);
       }catch(Exception e){
            logException(new Exception(movie.getTitle() + " Failed to store write."));
            System.out.println(e.toString());
        }
    }


    /**
     * Reads the movie history file and turns them into an ArrayList
     * @return ArrayList of Movies
     */
    protected static ArrayList<Movie> pullAllFromHistory() {
        ArrayList<String> movieIDs = readByLine(movieFileName);
        ArrayList<Movie> movies = new ArrayList<>();
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
    protected static boolean write(String s, String fileName) {
        if (s != null && !s.isEmpty()) {
            try {
                FileWriter file = new FileWriter(fileName, true);
                file.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m")));
                file.write(s + "\n");
                file.close();
                return true;
            } catch (Exception e) {
                System.out.println("There was an error writing to a file");
                System.out.println(e.toString());
                return false;
            }
        }
        else {
            return false;
        }
    }


    /**
     * Reads the targeted file line by line and returns an ArrayList of each line as a String
     * @param fileName is the file to read
     * @return ArrayList of Strings
     */
    protected static ArrayList<String> readByLine(String fileName) {
        try {
            ArrayList<String> output = new ArrayList<>();
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
