package com.tesla.code.repository;

import com.tesla.code.beans.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatusRepository extends PagingAndSortingRepository<JobStatus, String> {

    @Query(value = "select u from JobStatus u order by u.timestamp desc")
    public Page<JobStatus> getStatusList(Pageable pageable);
}
