package com.ebacon.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.ebacon.app.model.TimePunch;

public class TimePunchTest {
    @Test
    void testGettersAndSetters() {
        TimePunch punch = new TimePunch();

        punch.setJob("DEV");
        punch.setStart("2025-08-26T09:00");
        punch.setEnd("2025-08-26T17:00");

        // Assertions
        assertEquals("DEV", punch.getJob());
        assertEquals("2025-08-26T09:00", punch.getStart());
        assertEquals("2025-08-26T17:00", punch.getEnd());

        // Test toString
        String expected = "DEV: start=2025-08-26T09:00, end=2025-08-26T17:00";
        assertEquals(expected, punch.toString());
    }
}
