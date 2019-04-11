package com.group395.ember;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoggerTest {

    private Movie m1;

    private class AlexException extends Exception{

        public AlexException() {}

        public AlexException(String message) {
            super(message);
        }
    }

    @Before
    public void initialize() throws IOException {
        Logger log1 = new Logger();
        // Should write to line 1
        Logger.logException(new InvalidObjectException("Test Exception"));
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

    @Ignore
    public void testHistory() throws NullPointerException {
        ArrayList<Movie> history = Logger.pullAllFromHistory();
        assertEquals(history.get(0), m1);
    }

    @Ignore
    public void testWrite() throws NullPointerException {
        assertTrue(Logger.write("Some text", Logger.getFileName()));
        ArrayList<String> writeOut = Logger.readByLine(Logger.getFileName());
        assertTrue(writeOut.contains("Some text"));
        assertFalse(Logger.write(null, Logger.getFileName()));
    }
}
