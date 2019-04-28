package Validation.Result;

public class Named<T> implements Result<T>
{
    private String name;
    private T value;
    private Boolean successful;

    public Named(String name, T value, Boolean successful)
    {
        this.name = name;
        this.value = value;
        this.successful = successful;
    }

    @Override
    public Boolean isSuccessful()
    {
        return this.successful;
    }

    @Override
    public T value()
    {
        return this.value;
    }

    @Override
    public Boolean isNamed()
    {
        return true;
    }

    @Override
    public String name()
    {
        return this.name;
    }
}
