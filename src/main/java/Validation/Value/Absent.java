package Validation.Value;

public class Absent<T> implements Value<T>
{
    @Override
    public T raw() throws Exception
    {
        throw new Exception("Absent raw does not have a, uhm, raw.");
    }

    @Override
    public Boolean isPresent()
    {
        return Boolean.FALSE;
    }
}
