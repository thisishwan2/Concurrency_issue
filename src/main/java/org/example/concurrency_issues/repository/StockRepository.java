package org.example.concurrency_issues.repository;

import org.example.concurrency_issues.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // 비관적 잠금
    @Query("select s from Stock s where s.id = :id") // 네이티브 쿼리 사용
    Stock findByIdWithPessimisticLock(Long id);

    @Lock(LockModeType.OPTIMISTIC) // 낙관적 잠금
    @Query("select s from Stock s where s.id = :id") // 네이티브 쿼리 사용
    Stock findByIdWithOptimisticLock(Long id);
}
