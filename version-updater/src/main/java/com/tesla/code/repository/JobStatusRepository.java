package com.tesla.code.repository;

import com.tesla.code.beans.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatusRepository extends PagingAndSortingRepository<JobStatus, String> {

    @Query(value = "select u from JobStatus u order by u.timestamp desc")
    Page<JobStatus> getStatusList(Pageable pageable);

    @Query(value = "select u from JobStatus u where u.job.rollOut.id= :rollOutId and u.job.id = :jobId order by u.timestamp desc")
    Page<JobStatus> getStatusListForRolloutAndJob(Pageable pageable, @Param("rollOutId") String rollOutId,
                                                  @Param("jobId") String jobId);

    @Query(value = "select u from JobStatus u where u.job.rollOut.id= :rollOutId order by u.timestamp desc")
    Page<JobStatus> getStatusListForRollOut(Pageable pageable, @Param("rollOutId") String rollOutId);

    @Query(value = "select u from JobStatus u where u.job.id = :jobId order by u.timestamp desc")
    Page<JobStatus> getStatusListForJob(Pageable pageable, @Param("jobId") String jobId);
}
