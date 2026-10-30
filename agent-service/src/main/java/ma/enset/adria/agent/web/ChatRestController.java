package ma.enset.adria.agent.web;


import ma.enset.adria.agent.agent.BankAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    private final BankAgent bankAgent;

    public ChatRestController(BankAgent bankAgent) {
        this.bankAgent = bankAgent;
    }

    @GetMapping
    public String chat(@RequestParam String query) {
        return bankAgent.ask(query);
    }
}