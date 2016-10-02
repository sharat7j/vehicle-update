package com.tesla.code.repository;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  JobRepository extends PagingAndSortingRepository<Job, String> {

    @Query(value = "select u from JobStatus u where u.job = :id order by u.timestamp desc")
    public List<JobStatus> getJobStatusList(@Param("id") String identifier);

}
