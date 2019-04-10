package com.group395.ember;

import org.junit.Before;
import org.junit.Test;
import java.lang.Exception;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.NullPointerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LoggerTest {

    private Movie m1;

    @Before
    public void initialize() throws IOException {
        // Should write to line 1
        Logger.logException(new Exception("Test Exception"));
        // Should write to line 2
        m1 = new Movie("New Movie");
        Logger.saveToHistory(m1);

    }

    @Test
    public void testException() throws NullPointerException {
        ArrayList<String> exceptions = Logger.readByLine(Logger.getFileName());
        // The first line should be the exception
        assertTrue(exceptions.get(0).contains("Test Exception"));
    }

    @Test
    public void testHistory() throws NullPointerException {
        ArrayList<Movie> history = Logger.pullAllFromHistory();
        assertEquals(history.get(0), m1);
    }

    @Test
    public void testWrite() throws NullPointerException {
        assertTrue(Logger.write("Some text", Logger.getFileName()));
        ArrayList<String> writeOut = Logger.readByLine(Logger.getFileName());
        assertTrue(writeOut.get(1).contains("Some text"));
        assertFalse(Logger.write(null, Logger.getFileName()));
    }
}
