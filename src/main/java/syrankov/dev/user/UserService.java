package syrankov.dev.user;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import syrankov.dev.account.AccountService;

import java.util.*;

@Service
public class UserService {

    private final Map<Integer, User> userMap;
    private final Set<String> takenLogins;
    private int idCounter;
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.userMap = new HashMap<>();
        this.idCounter = 0;
        this.takenLogins = new HashSet<>();
        this.accountService = accountService;
    }

    public User createUser(String login){

        if(takenLogins.contains(login)){
            throw new IllegalArgumentException("User already exists with login=%s".formatted(login));
        }

        takenLogins.add(login);

        idCounter++;
        var newUser = new User(idCounter, login, new ArrayList<>());

        var newAccount = accountService.createAccount(idCounter);
        newUser.getAccountList().add(newAccount);

        userMap.put(newUser.getId(), newUser);

        return newUser;
    }

    public Optional<User> findUserById(int id){
        return Optional.ofNullable(userMap.get(id));
    }

    public List<User> getAllUsers(){
        return userMap.values().stream().toList();
    }
}
