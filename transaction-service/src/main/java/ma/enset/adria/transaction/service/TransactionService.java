package ma.enset.adria.transaction.service;

import ma.enset.adria.transaction.client.AccountDTO;
import ma.enset.adria.transaction.client.AccountRestClient;
import ma.enset.adria.transaction.domain.Transaction;
import ma.enset.adria.transaction.domain.TransactionType;
import ma.enset.adria.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRestClient accountRestClient;

    @Value("${bank.transaction.max-amount:1000}")
    private BigDecimal maxAmount;

    public TransactionService(TransactionRepository transactionRepository, AccountRestClient accountRestClient) {
        this.transactionRepository = transactionRepository;
        this.accountRestClient = accountRestClient;
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public Transaction create(UUID accountId, BigDecimal amount, String currency,
                              String merchantCategory, String country) {
        checkAmount(amount);
        AccountDTO account = loadActiveAccount(accountId);
        checkCurrency(account, currency);
        checkBalance(account, amount);

        Transaction tx = save(accountId, amount, currency, merchantCategory, country, TransactionType.DEBIT);
        accountRestClient.debit(accountId, amount.doubleValue());
        return tx;
    }

    public List<Transaction> transfer(UUID fromId, UUID toId, BigDecimal amount, String currency) {
        if (fromId.equals(toId)) throw new RuntimeException("Les deux comptes doivent être différents");
        checkAmount(amount);
        AccountDTO from = loadActiveAccount(fromId);
        AccountDTO to = loadActiveAccount(toId);
        checkCurrency(from, currency);
        checkCurrency(to, currency);
        checkBalance(from, amount);

        Transaction debit = save(fromId, amount, currency, "TRANSFER", null, TransactionType.DEBIT);
        Transaction credit = save(toId, amount, currency, "TRANSFER", null, TransactionType.CREDIT);
        accountRestClient.debit(fromId, amount.doubleValue());
        try {
            accountRestClient.credit(toId, amount.doubleValue());
        } catch (RuntimeException e) {
            accountRestClient.credit(fromId, amount.doubleValue()); // compensation
            throw e;
        }
        return List.of(debit, credit);
    }

    private Transaction save(UUID accountId, BigDecimal amount, String currency,
                             String merchantCategory, String country, TransactionType type) {
        return transactionRepository.save(Transaction.builder()
                .accountId(accountId)
                .amount(amount)
                .currency(currency.toUpperCase())
                .merchantCategory(merchantCategory)
                .country(country)
                .type(type)
                .createdAt(Instant.now())
                .build());
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new RuntimeException("Le montant doit être strictement positif");
        if (amount.compareTo(maxAmount) > 0)
            throw new RuntimeException("Le montant dépasse le plafond autorisé (" + maxAmount + ")");
    }

    private AccountDTO loadActiveAccount(UUID accountId) {
        AccountDTO account = accountRestClient.getAccount(accountId);
        if (!"ACTIVE".equals(account.status()))
            throw new RuntimeException("Le compte " + accountId + " n'est pas actif");
        return account;
    }

    private void checkCurrency(AccountDTO account, String currency) {
        if (!account.currency().equalsIgnoreCase(currency))
            throw new RuntimeException("La devise " + currency + " ne correspond pas à celle du compte (" + account.currency() + ")");
    }

    private void checkBalance(AccountDTO account, BigDecimal amount) {
        if (BigDecimal.valueOf(account.balance()).compareTo(amount) < 0)
            throw new RuntimeException("Solde insuffisant");
    }
}