package com.ericsson.training_project;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/data")
public class RestAPI {
    @Autowired
    private CsvRepositoryImpl csvRepository;

    @PostMapping("/")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file.");
        }
        try {
            CSV csv = ProcessCSVFile(file);
            csvRepository.insertCSV(csv);
            return ResponseEntity.ok().body("File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error. Please try again later");
        }
    }

    @GetMapping("/byUrl/")
    public ResponseEntity<Collection<CSV>> getDataByUrl(@RequestParam String url) {
        Collection<CSV> csvList = csvRepository.getCsvByUrl(url);
        return ResponseEntity.ok().body(csvList);
    }

    @GetMapping("/")
    public ResponseEntity<Collection<CSV>> getAllDataByDate() {
        Collection<CSV> csvList = csvRepository.getCSVs();
        return ResponseEntity.ok().body(csvList);
    }

    public CSV ProcessCSVFile(MultipartFile file) throws IOException {
        try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream())))) {
            List<String[]> lines = reader.readAll();
            lines = lines.subList(1, lines.size()); // Remove header line
            // Process each line
            for (String[] line : lines) {
                // Access elements of the line array
                String url = line[0];
                Date date = new Date();
                int commits = Integer.parseInt(line[1]);
                int linesAdded = Integer.parseInt(line[2]);
                int linesRemoved = Integer.parseInt(line[3]);
                double averageChangeSet = Double.parseDouble(line[4]);
                int maxChangeSet = Integer.parseInt(line[5]);
                double averageCodeChurn = Double.parseDouble(line[6]);
                int maxCodeChurn = Integer.parseInt(line[7]);
                int contributorsNum = Integer.parseInt(line[8]);
                String contributors = line[9];
                String smallContributors = line[10];
                int hunksCount = Integer.parseInt(line[11]);
                String commitData = line[12];
                // Create a new CSV object
                CSV csv = new CSV(url, date, commits, linesAdded, linesRemoved, averageChangeSet, maxChangeSet, averageCodeChurn, maxCodeChurn, contributorsNum, contributors, smallContributors, hunksCount, commitData);
                return csv;

                //store csv into database
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
