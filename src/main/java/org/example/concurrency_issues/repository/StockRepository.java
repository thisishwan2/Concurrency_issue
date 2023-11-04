package org.example.concurrency_issues.repository;

import org.example.concurrency_issues.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
