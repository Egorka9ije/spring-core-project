package syrankov.dev.operations;

public interface OperationCommandProcessor {

    void processOperation();

    ConsoleOperationType getOperationType();
}
