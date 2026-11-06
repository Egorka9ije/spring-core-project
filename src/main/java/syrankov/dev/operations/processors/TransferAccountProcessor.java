package syrankov.dev.operations.processors;

import org.springframework.stereotype.Component;
import syrankov.dev.account.AccountService;
import syrankov.dev.operations.ConsoleOperationType;
import syrankov.dev.operations.OperationCommandProcessor;

import java.util.Scanner;

@Component
public class TransferAccountProcessor implements OperationCommandProcessor {

    private final Scanner scanner;

    private final AccountService accountService;

    public TransferAccountProcessor(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void processOperation() {
        System.out.println("Enter the id of the account from which you want to transfer money: ");
        int accountFromWhich = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter the account ID to which you want to transfer money: ");
        int accountToTransfer = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter amount to transfer: ");
        int amountToTransfer = Integer.parseInt(scanner.nextLine());

        accountService.transferAccount(accountFromWhich, accountToTransfer, amountToTransfer);
        System.out.printf("Successfully transfer amount %s to id=%s from id=%s\n" , amountToTransfer, accountToTransfer, accountFromWhich);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
