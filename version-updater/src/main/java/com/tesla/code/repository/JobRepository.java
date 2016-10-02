package com.tesla.code.repository;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  JobRepository extends PagingAndSortingRepository<Job, String> {

    @Query(value = "select u from JobStatus u where u.job.id = :id order by u.timestamp desc")
    List<JobStatus> getJobStatusList(@Param("id") String identifier);

    @Query(value = "select u from Job u where u.rollOut.id = :rollOutId order by u.date_created desc")
    Page<Job> getJobsForRollOut(Pageable pageable, @Param("rollOutId") String rollOutId);

    @Query(value = "select u from Job u order by u.date_created desc")
    Page<Job> findAllSorted(Pageable pageable);
}
