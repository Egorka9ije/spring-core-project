package syrankov.dev.operations.processors;

import org.springframework.stereotype.Component;
import syrankov.dev.account.AccountService;
import syrankov.dev.operations.ConsoleOperationType;
import syrankov.dev.operations.OperationCommandProcessor;
import syrankov.dev.user.UserService;

import java.util.Scanner;

@Component

public class CreateAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;

    private final AccountService accountService;

    private final UserService userService;

    @Override
    public ConsoleOperationType getOperationType() {
         return ConsoleOperationType.ACCOUNT_CREATE;
    }

    public CreateAccountProcessor(Scanner scanner, AccountService accountService, UserService userService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter the user id for which to create an account: ");
        int userId = Integer.parseInt(scanner.nextLine());

        var user = userService.findUserById(userId)
                .orElseThrow(()->new IllegalArgumentException("No such user with id=%s".formatted(userId)));

        var account = accountService.createAccount(user.getId());

        user.getAccountList().add(account);

        System.out.println("New account created with id=%s for user: %s"
                .formatted(account.getId(), user.getLogin()));

    }
}
