package oss.cosc2440.rmit.seedwork;

/**
* @author Group 8
*/

public class ActionOption<T> extends InputOption {
    private T action;

    public ActionOption(String label, T action) {
        super(label);
        this.action = action;
    }

    public T getAction() {
        return action;
    }
}
