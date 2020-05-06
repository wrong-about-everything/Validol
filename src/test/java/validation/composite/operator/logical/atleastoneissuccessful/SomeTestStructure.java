package validation.composite.operator.logical.atleastoneissuccessful;
import java.util.Map;

final public class SomeTestStructure
{
    private Map<?, ?> map;
    private String firstField;
    private Boolean secondField;
    private Integer thirdField;
    private String unrelated;

    public SomeTestStructure(Map<?, ?> map, String unrelated)
    {
        this.map = map;
        this.unrelated = unrelated;
    }

    public Map<?, ?> map()
    {
        return this.map;
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
