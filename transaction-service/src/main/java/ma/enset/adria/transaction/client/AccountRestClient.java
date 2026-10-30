package ma.enset.adria.transaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "ACCOUNT-SERVICE", url = "${account-service.url:}",
        fallbackFactory = AccountRestClientFallbackFactory.class)
public interface AccountRestClient {

    @GetMapping("/api/accounts/{id}")
    AccountDTO getAccount(@PathVariable("id") UUID id);

    @PostMapping("/api/accounts/{id}/debit")
    AccountDTO debit(@PathVariable("id") UUID id, @RequestParam("amount") double amount);

    @PostMapping("/api/accounts/{id}/credit")
    AccountDTO credit(@PathVariable("id") UUID id, @RequestParam("amount") double amount);
}