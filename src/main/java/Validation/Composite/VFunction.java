package Validation.Composite;

@FunctionalInterface
public interface VFunction<T, R>
{
    public R apply(T argument) throws Exception;
}
