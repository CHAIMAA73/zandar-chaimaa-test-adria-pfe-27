package ma.enset.adria.account.service;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.account.domain.Account;
import ma.enset.adria.account.domain.AccountStatus;
import ma.enset.adria.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable : " + id));
    }

    public Account create(String customerReference, String currency, double balance) {
        Account account = Account.builder()
                .customerReference(customerReference)
                .currency(currency.toUpperCase())
                .status(AccountStatus.ACTIVE)
                .balance(balance)
                .build();
        return accountRepository.save(account);
    }

    public Account changeStatus(UUID id, AccountStatus status) {
        Account account = findById(id);
        account.setStatus(status);
        return account;
    }

    public Account debit(UUID id, double amount) {
        Account account = checkActive(id, amount);
        if (account.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }
        account.setBalance(account.getBalance() - amount);
        return account;
    }

    public Account credit(UUID id, double amount) {
        Account account = checkActive(id, amount);
        account.setBalance(account.getBalance() + amount);
        return account;
    }

    private Account checkActive(UUID id, double amount) {
        if (amount <= 0) throw new RuntimeException("Le montant doit être strictement positif");
        Account account = findById(id);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Le compte n'est pas actif");
        }
        return account;
    }
}