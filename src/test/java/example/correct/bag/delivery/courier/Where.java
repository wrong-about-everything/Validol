package example.correct.bag.delivery.courier;

public interface Where
{
    public Boolean isAbsent();

    public String street() throws Exception;

    public Integer building() throws Exception;
}
