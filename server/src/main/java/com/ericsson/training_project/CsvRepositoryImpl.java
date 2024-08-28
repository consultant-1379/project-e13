package com.ericsson.training_project;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.Modifying;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public class CsvRepositoryImpl implements CsvRepository{

    @PersistenceContext
    protected EntityManager entityManager;


    @Override
    public Collection<CSV> getCSVs() {
        String jpql = "select c.url from CSV c";
        TypedQuery<CSV> query = entityManager.createQuery(jpql, CSV.class);
        return query.getResultList();
    }

    @Override
    public Collection<CSV> getCsvByUrl(String url) {
        String jpql = "SELECT c FROM CSV c WHERE c.url = ?1";
        Collection<CSV> matchingCSVs = entityManager
                .createQuery(jpql, CSV.class)
                .setParameter(1, url)  // Set the value for the parameter
                .getResultList();

        return matchingCSVs;
    }

    @Override
    public long countCSVs() {
        String jpql = "SELECT COUNT(c) FROM CSV c";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void dropAllCsvs() {
        String nativeSql = "DELETE FROM CSV"; // Native SQL query to delete all entries
        Query query = entityManager.createQuery("DELETE FROM CSV");
        query.executeUpdate();
        }


    @Override
    @Transactional
    public void insertCSV(CSV csvFile) {
        entityManager.persist(csvFile);
        entityManager.flush();

    }
}
