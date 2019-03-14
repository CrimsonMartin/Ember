import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Logger {

    private String fileName;
    private File file;


    /**
     * Default constructor; uses a default file name: EmberLogDDMMYY
     */
    public Logger() {
        // Formatting date
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dMMMyy");
        fileName = "EmberLog" + date.format(dateFormat) + ".txt";

        file = new File(fileName);
    }

    /**
     * Constructor for a Logger, takes a String for a file name
     * @param fileName is a file to write logging details to
     */
    public Logger(String fileName) {
        this.fileName = fileName + ".txt";
        file = new File(this.fileName);
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
     * Logs a click on a Movie for "today"
     * @param movie to log for
     */
    public void logClick(Movie movie) {

    }

    /**
     * Returns the number of times a movie has been clicked "today"
     * @param movie to check
     * @return
     */
    public int getClickCountToday(Movie movie) {
        //TODO
        return -1;
    }

    /**
     * Saves a movie to the overall history
     * @param movie
     */
    public void saveToHistory(Movie movie) {

    }


    /**
     * Clears the clicks a movie has receieved "today"
     * @param movie to clear clicks for
     */
    public void clearClickCountToday(Movie movie) {

    }

    /**
     * Writes any String to this log
     * @param s is what to write
     * @return true if write was successful, false if exception thrown
     */
    public boolean write(String s) {
        try {
            FileWriter file = new FileWriter(fileName, true);
            file.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("H:m")));
            file.write(s + "\n");
            file.close();
            return true;
        }
        catch (Exception e) {
            System.out.println("There was an error writing to this file!");
            return false;
        }
    }


}
