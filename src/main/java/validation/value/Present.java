package validation.value;

final public class Present<T> implements Value<T>
{
    private T value;

    public Present(T value) throws Exception
    {
        if (value == null) {
            throw new Exception("Value can not be null");
        }

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
