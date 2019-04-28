package Validation.Result;

public interface Result<T>
{
    public Boolean isSuccessful();

    public T value();

    public Boolean isNamed();

    public String name();
}
