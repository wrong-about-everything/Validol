package Validation.ExampleRequest;

public class Where
{
    private String street;
    private Integer building;

    public Where(String street, Integer building)
    {
        this.street = street;
        this.building = building;
    }

    public String street()
    {
        return this.street;
    }

    public Integer building()
    {
        return this.building;
    }
}
