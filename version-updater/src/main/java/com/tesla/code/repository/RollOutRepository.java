package com.tesla.code.repository;

import com.tesla.code.beans.RollOut;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository
public interface RollOutRepository extends PagingAndSortingRepository<RollOut, String> {

    @Modifying
    @Transactional
    @Query(value = "delete from RollOut u where u.id = :id")
    void deleteRollOut(@Param("id") String id);
}
