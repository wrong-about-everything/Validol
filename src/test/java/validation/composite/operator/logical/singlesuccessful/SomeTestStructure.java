package validation.composite.operator.logical.singlesuccessful;

final public class SomeTestStructure
{
    private String firstField;
    private Boolean secondField;
    private Integer thirdField;
    private String unrelated;

    public SomeTestStructure(String firstField, String unrelated)
    {
        this.firstField = firstField;
        this.unrelated = unrelated;
    }

    public SomeTestStructure(Boolean secondField, String unrelated)
    {
        this.secondField = secondField;
        this.unrelated = unrelated;
    }

    public SomeTestStructure(Integer thirdField, String unrelated)
    {
        this.thirdField = thirdField;
        this.unrelated = unrelated;
    }

    public String firstField()
    {
        return this.firstField;
    }

    public Boolean secondField()
    {
        return this.secondField;
    }

    public Integer thirdField()
    {
        return this.thirdField;
    }

    public String unrelated()
    {
        return this.unrelated;
    }
}
