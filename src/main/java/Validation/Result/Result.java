package Validation.Result;

public interface Result<T>
{
    public Boolean isSuccessful();

    public T value() throws Exception;

    public String error() throws Exception;

    public Boolean isNamed();

    public String name() throws Exception;
}
