package com.tesla.code.repository;

import com.tesla.code.beans.Job;
import com.tesla.code.beans.RollOut;
import com.tesla.code.utils.JobState;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RollOutRepository extends PagingAndSortingRepository<RollOut, String> {

    @Modifying
    @Transactional
    @Query(value = "delete from RollOut u where u.id = :id")
    void deleteRollOut(@Param("id") String id);

    @Query(value = "select count(u) from JobStatus u where u.job.rollOut.id = :id")
    Long getJobStatusCount(@Param("id") String id);

    @Query(value = "select count(u) from Job u where u.rollOut.id = :id")
    Long getJobCount(@Param("id") String id);

    @Query(value = "select count(u) from JobStatus u where u.state = :state and u.job.rollOut.id = :id")
    Integer getStateCount(@Param("state") JobState state, @Param("id") String id);

    @Query(value = "select u from Job u where u.rollOut.id = :id")
    List<Job> getJobsForRollOut(@Param("id") String id);

    @Query(value = "select u from RollOut u where u.name = :name")
    RollOut nameExists(@Param("name") String name);
}
