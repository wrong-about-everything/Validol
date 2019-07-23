package validation.composite;

@FunctionalInterface
public interface VFunction<T, R>
{
    public R apply(T argument) throws Throwable;
}
