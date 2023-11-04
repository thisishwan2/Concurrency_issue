package org.example.concurrency_issues.facade;

import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.repository.RedisLockRepository;
import org.example.concurrency_issues.service.StockService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while(!redisLockRepository.lock(key)){
            Thread.sleep(100);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }

    }
}
