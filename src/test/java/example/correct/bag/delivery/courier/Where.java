package example.correct.bag.delivery.courier;

public interface Where
{
    public Boolean isAbsent();

    public String street() throws Throwable;

    public Integer building() throws Throwable;
}
