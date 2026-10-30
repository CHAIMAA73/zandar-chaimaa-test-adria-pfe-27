package ma.enset.adria.transaction.mcp;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.transaction.domain.Transaction;
import ma.enset.adria.transaction.service.TransactionService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionMcpTools {

    private final TransactionService transactionService;

    @Tool(description = "Lister toutes les transactions bancaires")
    public List<Transaction> listTransactions() {
        return transactionService.findAll();
    }

    @Tool(description = "Lister les transactions d'un compte")
    public List<Transaction> transactionsByAccount(@ToolParam(description = "UUID du compte") String accountId) {
        return transactionService.findByAccount(UUID.fromString(accountId));
    }

    @Tool(description = "Effectuer un paiement (débit) sur un compte")
    public Transaction createTransaction(@ToolParam(description = "UUID du compte") String accountId,
                                         @ToolParam(description = "Montant") double amount,
                                         @ToolParam(description = "Devise, ex: MAD") String currency,
                                         @ToolParam(description = "Catégorie marchand", required = false) String merchantCategory,
                                         @ToolParam(description = "Code pays, ex: MA", required = false) String country) {
        return transactionService.create(UUID.fromString(accountId), BigDecimal.valueOf(amount),
                currency, merchantCategory, country);
    }

    @Tool(description = "Effectuer un virement d'un compte vers un autre")
    public List<Transaction> transfer(@ToolParam(description = "UUID du compte source") String fromAccountId,
                                      @ToolParam(description = "UUID du compte destination") String toAccountId,
                                      @ToolParam(description = "Montant") double amount,
                                      @ToolParam(description = "Devise") String currency) {
        return transactionService.transfer(UUID.fromString(fromAccountId), UUID.fromString(toAccountId),
                BigDecimal.valueOf(amount), currency);
    }
}