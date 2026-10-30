package ma.enset.adria.transaction.web.rest;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.transaction.domain.Transaction;
import ma.enset.adria.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> findByAccount(@PathVariable UUID accountId) {
        return transactionService.findByAccount(accountId);
    }

    @PostMapping
    public Transaction create(@RequestParam UUID accountId,
                              @RequestParam BigDecimal amount,
                              @RequestParam String currency,
                              @RequestParam(required = false) String merchantCategory,
                              @RequestParam(required = false) String country) {
        return transactionService.create(accountId, amount, currency, merchantCategory, country);
    }

    @PostMapping("/transfer")
    public List<Transaction> transfer(@RequestParam UUID fromAccountId,
                                      @RequestParam UUID toAccountId,
                                      @RequestParam BigDecimal amount,
                                      @RequestParam String currency) {
        return transactionService.transfer(fromAccountId, toAccountId, amount, currency);
    }

    @GetMapping("/max-amount")
    public Map<String, BigDecimal> maxAmount() {
        return Map.of("bank.transaction.max-amount", transactionService.getMaxAmount());
    }
}