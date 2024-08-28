package com.ericsson.training_project;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "csvstats")
public class CSV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private Date date;
    // -----

    private int commits;
    private int linesAdded;
    private int linesRemoved;
    private double averageChangeSet;
    private int maxChangeSet;
    private double averageCodeChurn;
    private int maxCodeChurn;
    private int contributorsNum;
    private String contributors;
    private String smallContributors;
    private int hunksCount;
    @Column(length = 999999999)
    private String commitData;

    public CSV(){}

    public CSV(String url, Date date, int commits, int linesAdded, int linesRemoved, double averageChangeSet,
               int maxChangeSet, double averageCodeChurn, int maxCodeChurn, int contributorsNum,
               String contributors, String smallContributors, int hunksCount,
               String commitData) {
        this.url = url;
        this.date = date;
        this.commits = commits;
        this.linesAdded = linesAdded;
        this.linesRemoved = linesRemoved;
        this.averageChangeSet = averageChangeSet;
        this.maxChangeSet = maxChangeSet;
        this.averageCodeChurn = averageCodeChurn;
        this.maxCodeChurn = maxCodeChurn;
        this.contributorsNum = contributorsNum;
        this.contributors = contributors;
        this.hunksCount = hunksCount;
        this.smallContributors = smallContributors;
        this.commitData = commitData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public int getLinesAdded() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded = linesAdded;
    }

    public int getLinesRemoved() {
        return linesRemoved;
    }

    public void setLinesRemoved(int linesRemoved) {
        this.linesRemoved = linesRemoved;
    }

    public double getAverageChangeSet() {
        return averageChangeSet;
    }

    public void setAverageChangeSet(int averageChangeSet) {
        this.averageChangeSet = averageChangeSet;
    }

    public int getMaxChangeSet() {
        return maxChangeSet;
    }

    public void setMaxChangeSet(int maxChangeSet) {
        this.maxChangeSet = maxChangeSet;
    }

    public double getAverageCodeChurn() {
        return averageCodeChurn;
    }

    public void setAverageCodeChurn(int averageCodeChurn) {
        this.averageCodeChurn = averageCodeChurn;
    }

    public int getMaxCodeChurn() {
        return maxCodeChurn;
    }

    public void setMaxCodeChurn(int maxCodeChurn) {
        this.maxCodeChurn = maxCodeChurn;
    }

    public int getContributorsNum() {
        return contributorsNum;
    }

    public void setContributorsNum(int contributorsNum) {
        this.contributorsNum = contributorsNum;
    }

    public String getContributors() {
        return contributors;
    }

    public void setContributors(String contributors) {
        this.contributors = contributors;
    }

    public int getHunksCount() {
        return hunksCount;
    }

    public void setHunksCount(int hunksCount) {
        this.hunksCount = hunksCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSmallContributors() {
        return smallContributors;
    }

    public void setSmallContributors(String smallContributors) {
        this.smallContributors = smallContributors;
    }

    public String getCommitData() {
        return commitData;
    }

    public void setCommitData(String commitData) {
        this.commitData = commitData;
    }
}
