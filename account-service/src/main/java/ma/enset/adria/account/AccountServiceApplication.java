package ma.enset.adria.account;

import ma.enset.adria.account.domain.Account;
import ma.enset.adria.account.domain.AccountStatus;
import ma.enset.adria.account.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(AccountRepository repository) {
        return args -> {
            repository.save(Account.builder().customerReference("C-001").status(AccountStatus.ACTIVE).currency("MAD").balance(5000).build());
            repository.save(Account.builder().customerReference("C-002").status(AccountStatus.ACTIVE).currency("MAD").balance(3000).build());
            repository.save(Account.builder().customerReference("C-003").status(AccountStatus.ACTIVE).currency("EUR").balance(2000).build());
            repository.save(Account.builder().customerReference("C-004").status(AccountStatus.BLOCKED).currency("MAD").balance(1000).build());
        };
    }
}