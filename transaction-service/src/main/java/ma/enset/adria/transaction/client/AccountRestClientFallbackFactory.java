package ma.enset.adria.transaction.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AccountRestClientFallbackFactory implements FallbackFactory<AccountRestClient> {

    @Override
    public AccountRestClient create(Throwable cause) {
        log.warn("Fallback Resilience4J : {}", cause.toString());
        RuntimeException error = new RuntimeException(
                "account-service indisponible ou opération refusée : " + cause.getMessage());
        return new AccountRestClient() {
            @Override
            public AccountDTO getAccount(UUID id) { throw error; }

            @Override
            public AccountDTO debit(UUID id, double amount) { throw error; }

            @Override
            public AccountDTO credit(UUID id, double amount) { throw error; }
        };
    }
}