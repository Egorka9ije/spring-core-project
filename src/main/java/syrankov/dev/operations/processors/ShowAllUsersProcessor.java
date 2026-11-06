package syrankov.dev.operations.processors;

import org.springframework.stereotype.Component;
import syrankov.dev.operations.ConsoleOperationType;
import syrankov.dev.operations.OperationCommandProcessor;
import syrankov.dev.user.User;
import syrankov.dev.user.UserService;

import java.util.List;

@Component
public class ShowAllUsersProcessor implements OperationCommandProcessor {

    private final UserService userService;

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }

    public ShowAllUsersProcessor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void processOperation() {
        List<User> users = userService.getAllUsers();
        System.out.println("List of all users:");
        users.forEach(System.out::println);
    }
}
