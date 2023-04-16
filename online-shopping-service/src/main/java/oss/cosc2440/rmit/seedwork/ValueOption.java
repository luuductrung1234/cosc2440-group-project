package oss.cosc2440.rmit.seedwork;

/**
 * @author Luu Duc Trung - S3951127
 */
public class ValueOption<T> extends InputOption {
    private T value;

    public ValueOption(String label, T value) {
        super(label);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
