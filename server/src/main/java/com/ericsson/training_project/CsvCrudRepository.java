package com.ericsson.training_project;

import org.springframework.data.repository.CrudRepository;

public interface CsvCrudRepository extends CrudRepository <CSV, Long> {

    // need only insert and get


}
