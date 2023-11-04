package org.example.concurrency_issues.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.domain.Stock;
import org.example.concurrency_issues.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
public class PessimisticLockStockService {

    private final StockRepository stockRepository;


    @Transactional
    public void decrease(Long id, Long quantity) { //synchronized 키워드를 추가하여 동시에 접근하는 것을 막는다.
        // 재고 조회
        // 재고 감소
        // 갱신된 값 저장
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
