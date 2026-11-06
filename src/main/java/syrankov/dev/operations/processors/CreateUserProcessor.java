package syrankov.dev.operations.processors;

import org.springframework.stereotype.Component;
import syrankov.dev.operations.ConsoleOperationType;
import syrankov.dev.operations.OperationCommandProcessor;
import syrankov.dev.user.User;
import syrankov.dev.user.UserService;

import java.util.Scanner;

@Component
public class CreateUserProcessor implements OperationCommandProcessor {

    private final Scanner scanner;

    private final UserService userService;

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }

    public CreateUserProcessor(Scanner scanner, UserService userService) {
        this.scanner = scanner;
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter login for new user:");
        String login = scanner.nextLine();
        User user = userService.createUser(login);
        System.out.println("User created: " + user.toString());
    }
}
