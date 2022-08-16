package com.sigma.clotheswarehouse.repository;

import com.sigma.clotheswarehouse.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BorrowRepository extends JpaRepository<Borrow, UUID> {


    List<Borrow> findByDeletedFalse();
    List<Borrow> findByClient_IdAndDeletedIsFalseOrderByBeginDateAsc(Long id);



    @Query(value = "select * from borrow b where b.client_id =:client_id and b.deleted=:deleted order by begin_date asc ",nativeQuery = true)
    List<Borrow> getBorrowListByClient_Id(@Param(value = "client_id" )Long client_id , @Param(value = "deleted")boolean deleted);


    List<Borrow> findByClient_IdAndDeletedIsFalse(Long id);
}
