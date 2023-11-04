package org.example.concurrency_issues.facade;

import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.service.OptimisticLockStockService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    // update를 실패했을때, 재시도하는 로직
    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;
            }catch (Exception e){
                Thread.sleep(50);
            }
        }
    }
}
