package ma.enset.adria.account.web.graphql;

import lombok.RequiredArgsConstructor;
import ma.enset.adria.account.domain.Account;
import ma.enset.adria.account.domain.AccountStatus;
import ma.enset.adria.account.service.AccountService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {

    private final AccountService accountService;

    @QueryMapping
    public List<Account> accounts() {
        return accountService.findAll();
    }

    @QueryMapping
    public Account accountById(@Argument UUID id) {
        return accountService.findById(id);
    }

    @MutationMapping
    public Account createAccount(@Argument String customerReference,
                                 @Argument String currency,
                                 @Argument double balance) {
        return accountService.create(customerReference, currency, balance);
    }

    @MutationMapping
    public Account changeAccountStatus(@Argument UUID id, @Argument AccountStatus status) {
        return accountService.changeStatus(id, status);
    }
}