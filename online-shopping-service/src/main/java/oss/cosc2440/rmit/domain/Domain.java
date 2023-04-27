package oss.cosc2440.rmit.domain;

/**
* @author Group 8
*/

public abstract class Domain<TId> {
    protected final TId id;

    public Domain(TId id) {
        this.id = id;
    }

    /**
     * Convert instance of this class into string (serialization)
     *
     * @return a string contains data of Domain serialized instance
     */
    public String serialize() {
        return String.format("%s", id);
    }

    /**
     * This static method will be overridden by subclass(es)
     *
     * @param data serialized string data
     * @return new instance of Account
     */
    public static <TId> Domain<TId> deserialize(String data) {
        throw new UnsupportedOperationException("deserialize() has not been implemented!");
    }

    // Getter methods
    public TId getId() {
        return id;
    }
}
