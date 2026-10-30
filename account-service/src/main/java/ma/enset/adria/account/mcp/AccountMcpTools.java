package ma.enset.adria.account.mcp;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.account.domain.Account;
import ma.enset.adria.account.domain.AccountStatus;
import ma.enset.adria.account.service.AccountService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountMcpTools {

    private final AccountService accountService;

    @Tool(description = "Lister tous les comptes bancaires")
    public List<Account> listAccounts() {
        return accountService.findAll();
    }

    @Tool(description = "Consulter un compte bancaire par son identifiant UUID")
    public Account getAccount(@ToolParam(description = "UUID du compte") String accountId) {
        return accountService.findById(UUID.fromString(accountId));
    }

    @Tool(description = "Bloquer un compte bancaire")
    public Account blockAccount(@ToolParam(description = "UUID du compte") String accountId) {
        return accountService.changeStatus(UUID.fromString(accountId), AccountStatus.BLOCKED);
    }

    @Tool(description = "Activer un compte bancaire")
    public Account activateAccount(@ToolParam(description = "UUID du compte") String accountId) {
        return accountService.changeStatus(UUID.fromString(accountId), AccountStatus.ACTIVE);
    }
}