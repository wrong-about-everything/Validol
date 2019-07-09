package validation.value;

final public class Absent<T> implements Value<T>
{
    @Override
    public T raw() throws Exception
    {
        throw new Exception("value is absent.");
    }

    @Override
    public Boolean isPresent()
    {
        return Boolean.FALSE;
    }
}
