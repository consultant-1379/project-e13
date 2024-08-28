package com.ericsson.training_project;

import jakarta.transaction.Transactional;
import org.hibernate.boot.model.source.spi.SingularAttributeSourceToOne;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.SQLOutput;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CsvRepositoryIntegrationTest {

    @Autowired
    private CsvRepository csvRepository;


    @Test
    public void testDelete(){
        System.out.println(csvRepository.countCSVs() + "--- count ");
        csvRepository.dropAllCsvs();
        System.out.println(csvRepository.countCSVs() + "--- count after deletion ");

        assertEquals(0, csvRepository.countCSVs());

    }



    @Test
    @Transactional
    public void testInsertCSVAndCheckIfPresent() {
        Date date = new Date();
        CSV csv = new CSV("https://example.com/csv", date, 10, 20, 5,
                15, 30, 25, 3, 8,
                "Contributors", "Small Contributors", 7, "Commit Data");

        // Insert the CSV instance into the database
        csvRepository.insertCSV(csv);

        // Retrieve the CSV instance from the database by URL
        Collection<CSV> retrievedCSVs = csvRepository.getCsvByUrl("https://example.com/csv");

        // Check if the CSV instance is present
        assertNotNull(retrievedCSVs);

        assertEquals("https://example.com/csv", csv.getUrl());
        csvRepository.dropAllCsvs();
     }

    @Test
    @Transactional
    public void testMultipleCSVs() {
        Date date = new Date();
        CSV csv1 = new CSV("https://example.com/csv", date, 10, 20, 5,
                15, 30, 25, 3, 8,
                "Contributors", "Small Contributors", 7, "Commit Data");

        CSV csv2 = new CSV("https://example.com/csv", date, 5, 300, 9,
                4, 12, 70, 2, 9,
                "Contributors", "Small Contributors", 7, "Commit Data");

        csvRepository.insertCSV(csv1);
        csvRepository.insertCSV(csv2);

        assertEquals(2, csvRepository.getCsvByUrl("https://example.com/csv").size());
        assertEquals(2, csvRepository.getCSVs().size());
        csvRepository.dropAllCsvs();
    }

    @Test
    public void testCountOfCSVs() {
        Long currentCount = csvRepository.countCSVs();
        System.out.println(currentCount + " -- CSVs retrieved");

        Date date = new Date();
        CSV csv = new CSV("https://example.com/csv", date, 10, 20, 5,
                15, 30, 25, 3, 8,
                "Contributors", "Small Contributors", 7, "Commit Data");

        csvRepository.insertCSV(csv);

        Long updatedCount = csvRepository.countCSVs();
        System.out.println(updatedCount + " -- updated count of CSVs retrieved");

        assertEquals(currentCount+1, csvRepository.countCSVs());
        csvRepository.dropAllCsvs();

    }

}
