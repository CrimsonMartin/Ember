package com.group395.ember;

import org.junit.Before;
import org.junit.Test;
import java.lang.Exception;
import java.util.ArrayList;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LoggerTest {

    private Logger log1;
    private Movie m1;

    @Before
    public void initialize() throws IOException {
        log1 = new Logger();
        // Should write to line 1
        log1.logException(new Exception("Test Exception"));
        // Should write to line 2
        m1 = new Movie("New Movie");
        log1.saveToHistory(m1);

    }

    @Test
    public void testException() throws IOException {
        ArrayList<String> exceptions = log1.readByLine(Logger.getFileName());
        // The first line should be the exception
        assertTrue(exceptions.get(0).contains("Test Exception"));
    }

    @Test
    public void testHistory() throws IOException {
        ArrayList<Movie> history = Logger.pullAllFromHistory();
        assertEquals(history.get(0), m1);
    }

    @Test
    public void testWrite() throws IOException {
        boolean writeStatus = log1.write("Some text", Logger.getFileName());
        assertTrue(writeStatus);
        ArrayList<String> writeOut = log1.readByLine(Logger.getFileName());
        assertTrue(writeOut.get(1).contains("Some text"));

        boolean nullStatus = log1.write(null, Logger.getFileName());
        assertFalse(nullStatus);
    }
}
