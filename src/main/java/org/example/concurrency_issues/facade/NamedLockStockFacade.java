package org.example.concurrency_issues.facade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.concurrency_issues.repository.LockRepository;
import org.example.concurrency_issues.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
public class NamedLockStockFacade {

    private final LockRepository lockRepository;

    private final StockService stockService;

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
