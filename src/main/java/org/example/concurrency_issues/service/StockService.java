package org.example.concurrency_issues.service;

import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.domain.Stock;
import org.example.concurrency_issues.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class StockService {

    private final StockRepository stockRepository;

//    @Transactional 트랜잭션은 현재 트랜잭션을 선언한 클래스(StockService)를 필드로 가지는 클래스를 새로 만들어서 실행함(AOP)
//    트랜잭션 종료 시점에 DB 업데이트를 하는데, race condition으로 인해 DB 커밋전 다른 스레드가 dcrease 메서드를 실행시킬 수 있게된다.
//    public synchronized void decrease(Long id, Long quantity) { //synchronized 키워드를 추가하여 동시에 접근하는 것을 막는다.

    // stockservice에서는 부모의 트랜잭션과 별도로 실행이 되어야 하기 때문에 propagation을 변경
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long id, Long quantity) {
        // 재고 조회
        // 재고 감소
        // 갱신된 값 저장
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
