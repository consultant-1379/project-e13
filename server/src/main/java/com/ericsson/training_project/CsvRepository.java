package com.ericsson.training_project;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface CsvRepository {

    // Get all CSVs from the database.
    Collection<CSV> getCSVs();

    Collection<CSV> getCsvByUrl(String url);

    public long countCSVs();

    public void dropAllCsvs();
    @Transactional
    void insertCSV(CSV csvFile);


}
