package discord.wosaj.lambda;

public class Attribute<T> {
    private final String name;
    private T data;

    public Attribute(String name, T data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

