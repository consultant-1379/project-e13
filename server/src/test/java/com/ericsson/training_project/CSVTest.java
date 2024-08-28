package com.ericsson.training_project;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CSVTest {

    @Test
    public void testGettersAndSetters() {
        // Create an instance of the CSV class
        CSV csv = new CSV();

        csv.setUrl("https://example.com/csv");
        assertEquals("https://example.com/csv", csv.getUrl());

        csv.setDate(new Date());
        assertNotNull(csv.getDate());

        csv.setCommits(10);
        assertEquals(10, csv.getCommits());

        // more getter and setter tests

    }

    @Test
    public void testConstructor() {
        // Create an instance of the CSV class using the constructor
        Date date = new Date();
        CSV csv = new CSV("https://example.com/csv", date, 10, 20, 5,
                15, 30, 25, 3, 8,
                "Contributors", "Small Contributors", 7, "Commit Data");




        // Test that the constructor sets the properties correctly
        assertEquals("https://example.com/csv", csv.getUrl());
        assertEquals(date, csv.getDate());
        assertEquals(10, csv.getCommits());
        assertEquals(20, csv.getLinesAdded());
        assertEquals(5, csv.getLinesRemoved());
        assertEquals(15, csv.getAverageChangeSet());
        assertEquals(30, csv.getMaxChangeSet());
        assertEquals(25, csv.getAverageCodeChurn());
        assertEquals(3, csv.getMaxCodeChurn());
        assertEquals(8, csv.getContributorsNum());
        assertEquals("Contributors", csv.getContributors());
        assertEquals("Small Contributors", csv.getSmallContributors());
        assertEquals(7, csv.getHunksCount());
        assertEquals("Commit Data", csv.getCommitData());
    }

}

