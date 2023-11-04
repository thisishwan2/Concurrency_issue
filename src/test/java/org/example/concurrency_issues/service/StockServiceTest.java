package org.example.concurrency_issues.service;

import org.example.concurrency_issues.domain.Stock;
import org.example.concurrency_issues.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    // 테스트 시행전 재고가 존재해야 하므로 재고를 미리 생성해둔다.
    @BeforeEach
    public void before(){
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    // 테스트 종료후 모든 재고를 삭제한다.
    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소() {
        stockService.decrease(1L, 1L);

        // 100-1=99가 되어야 한다.
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99L, stock.getQuantity());
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100; // 100개의 스레드를 생성한다.
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 비동기 실행을 단순화하여 실행하도록 도와주는 자바 API

        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 스레드에서 수행중인 작업이 끝날때 대기할 수 있도록 도와주는 클래스이다.

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() ->{
                    try{
                        stockService.decrease(1L, 1L);
                    }finally {
                        latch.countDown();
                    }
            });
        }
        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        // 100-1*100=0이 되어야 한다.
        assertEquals(0L, stock.getQuantity());
    }
}