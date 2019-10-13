package validation.value;

final public class Present<T> implements Value<T>
{
    private T value;

    public Present(T value)
    {
        this.value = value;
    }

    @Override
    public T raw() throws Exception
    {
        return this.value;
    }

    @Override
    public Boolean isPresent()
    {
        return Boolean.TRUE;
    }
}
