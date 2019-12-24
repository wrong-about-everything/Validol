package example.incorrect.parameters.order;

final public class SampleRequest
{
    private Integer key1;
    private String key2;

    public SampleRequest(String key2, Integer key1)
    {
        this.key1 = key1;
        this.key2 = key2;
    }

    public Integer key1()
    {
        return this.key1;
    }

    public String key2()
    {
        return this.key2;
    }
}
