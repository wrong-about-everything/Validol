package validation.result.value;

public interface Value<T>
{
    public T raw() throws Exception;

    public Boolean isPresent();
}
