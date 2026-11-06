package syrankov.dev.operations.processors;

import org.springframework.stereotype.Component;
import syrankov.dev.account.AccountService;
import syrankov.dev.operations.ConsoleOperationType;
import syrankov.dev.operations.OperationCommandProcessor;

import java.util.Scanner;

@Component
public class DepositAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;

    private final AccountService accountService;

    public DepositAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter account id: ");
        int accountId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter amount to deposit: ");
        int amountToDeposit = Integer.parseInt(scanner.nextLine());
        accountService.depositAccount(accountId, amountToDeposit);
        System.out.printf("Successfully deposit amount %s to id=%s%n", amountToDeposit,accountId);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
