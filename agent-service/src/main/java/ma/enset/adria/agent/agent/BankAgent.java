package ma.enset.adria.agent.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class BankAgent {

    private static final String SYSTEM_PROMPT = """
            Tu es un assistant bancaire. Tu réponds en français.
            Utilise uniquement les outils disponibles pour consulter les comptes,
            lister les transactions, effectuer des paiements et des virements.
            N'invente jamais d'identifiant, de solde ou de transaction.
            Si l'utilisateur donne une référence client (ex: C-001), retrouve d'abord
            l'UUID du compte avec l'outil listAccounts.
            Si un outil renvoie une erreur, explique-la simplement à l'utilisateur.
            """;

    private final ChatClient chatClient;

    public BankAgent(ChatClient.Builder builder, ToolCallbackProvider mcpTools) {
        ChatMemory memory = MessageWindowChatMemory.builder().maxMessages(20).build();
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultToolCallbacks(mcpTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }

    public String ask(String question) {
        return chatClient.prompt().user(question).call().content();
    }
}