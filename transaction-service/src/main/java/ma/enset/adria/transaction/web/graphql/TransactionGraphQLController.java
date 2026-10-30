package ma.enset.adria.transaction.web.graphql;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.transaction.domain.Transaction;
import ma.enset.adria.transaction.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.findAll();
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(@Argument UUID accountId) {
        return transactionService.findByAccount(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument UUID accountId, @Argument double amount,
                                         @Argument String currency, @Argument String merchantCategory,
                                         @Argument String country) {
        return transactionService.create(accountId, BigDecimal.valueOf(amount), currency, merchantCategory, country);
    }

    @MutationMapping
    public List<Transaction> transfer(@Argument UUID fromAccountId, @Argument UUID toAccountId,
                                      @Argument double amount, @Argument String currency) {
        return transactionService.transfer(fromAccountId, toAccountId, BigDecimal.valueOf(amount), currency);
    }
}