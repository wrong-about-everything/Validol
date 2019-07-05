package Validation.Value;

public class Absent<T> implements Value<T>
{
    @Override
    public T raw() throws Exception
    {
        throw new Exception("Value is absent.");
    }

    @Override
    public Boolean isPresent()
    {
        return Boolean.FALSE;
    }
}
