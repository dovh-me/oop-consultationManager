package gui.components;

public interface InputGroup<T> {
    boolean validateInput();
    T getInput();
}
