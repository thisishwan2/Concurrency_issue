package org.example.concurrency_issues.facade;

import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public void decrease(Long id, Long quantity){
        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean avaliable = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if (!avaliable) {
                System.out.println("lock 획득 실패");
            }

            stockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
