package ma.enset.adria.account.web.rest;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.account.domain.Account;
import ma.enset.adria.account.domain.AccountStatus;
import ma.enset.adria.account.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable UUID id) {
        return accountService.findById(id);
    }

    @PostMapping
    public Account create(@RequestParam String customerReference,
                          @RequestParam String currency,
                          @RequestParam double balance) {
        return accountService.create(customerReference, currency, balance);
    }

    @PatchMapping("/{id}/status")
    public Account changeStatus(@PathVariable UUID id, @RequestParam AccountStatus status) {
        return accountService.changeStatus(id, status);
    }

    @PostMapping("/{id}/debit")
    public Account debit(@PathVariable UUID id, @RequestParam double amount) {
        return accountService.debit(id, amount);
    }

    @PostMapping("/{id}/credit")
    public Account credit(@PathVariable UUID id, @RequestParam double amount) {
        return accountService.credit(id, amount);
    }
}