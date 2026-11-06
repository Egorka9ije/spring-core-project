package syrankov.dev.account;

import org.springframework.stereotype.Service;
import syrankov.dev.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    private final Map<Integer, Account> accountMap;

    private int idCounter;

    private final AccountProperties accountProperties;

    public AccountService(AccountProperties accountProperties) {
        this.accountMap = new HashMap<>();
        this.idCounter = 0;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(int userId){
        idCounter++;
        Account account = new Account(idCounter, userId, accountProperties.getDefaultAmount());
        accountMap.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findAccountById(int id){
        return Optional.ofNullable(accountMap.get(id));

    }

    public List<Account> getAllUserAccounts(int userId){
        return accountMap.values()
                .stream()
                .filter(account -> account.getUserId() == userId)
                .toList();
    }

    public void depositAccount(int accountId, int moneyToDeposit){
        var account = findAccountById(accountId)
                .orElseThrow(()-> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        if(moneyToDeposit <= 0){
            throw new IllegalArgumentException("Cannot deposit not positive amount, amount=%s"
                    .formatted(moneyToDeposit));
        }

        account.setMoneyAmount(account.getMoneyAmount() + moneyToDeposit);
    }

    public void withdrawFromAccount(int accountId, int moneyToWithdraw){
        var account = findAccountById(accountId)
                .orElseThrow(()-> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        if(moneyToWithdraw <= 0){
            throw new IllegalArgumentException("Cannot withdraw not positive amount, amount=%s"
                    .formatted(moneyToWithdraw));
        }

        if(moneyToWithdraw > account.getMoneyAmount()){
            throw new IllegalArgumentException(
                    "Cannot withdraw from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                            .formatted(accountId,account.getMoneyAmount(),moneyToWithdraw));

        }

        account.setMoneyAmount(account.getMoneyAmount() - moneyToWithdraw);
    }

    public Account closeAccount(int accountId){
        var accountToRemove  = findAccountById(accountId)
                .orElseThrow(()-> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

        List<Account> accountList = getAllUserAccounts(accountToRemove.getUserId());

        if (accountList.size()==1){
            throw new IllegalArgumentException("Cannot close the only one account");
        }

        Account accountToDeposit = accountList.stream()
                .filter(it->it.getId() != accountId)
                .findFirst()
                .orElseThrow();

        accountToDeposit.setMoneyAmount(accountToDeposit.getMoneyAmount() + accountToRemove.getMoneyAmount());

        accountMap.remove(accountId);
        return accountToRemove;
    }

    public void transferAccount(int accountFromWhich, int accountToTransfer, int moneyToTransfer){
        var account1 = findAccountById(accountFromWhich)
                .orElseThrow(()-> new IllegalArgumentException("No such account: id=%s".formatted(accountFromWhich)));

        var account2 = findAccountById(accountToTransfer)
                .orElseThrow(()-> new IllegalArgumentException("No such account: id=%s".formatted(accountToTransfer)));

        boolean sameUser = account1.getUserId() == account2.getUserId();

        double commission = sameUser
                ? 0
                : moneyToTransfer * accountProperties.getTransferCommission() ;

        double totalWithdraw = moneyToTransfer + commission;

        if(moneyToTransfer <= 0){

            throw new IllegalArgumentException("Cannot transfer not positive amount, amount=%s"
                    .formatted(moneyToTransfer));

        } else if(totalWithdraw > account1.getMoneyAmount()) {

            throw new IllegalArgumentException(
                    "Cannot transfer from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                            .formatted(account1.getUserId(),account1.getMoneyAmount(),totalWithdraw));

        }



        account2.setMoneyAmount(account2.getMoneyAmount() + moneyToTransfer);
        account1.setMoneyAmount(account1.getMoneyAmount() - (int) totalWithdraw);
    }


}
