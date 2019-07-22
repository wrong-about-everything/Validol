package example.correct.bag.delivery.courier.where;

public class Where implements example.correct.bag.delivery.courier.Where
{
    private String street;
    private Integer building;

    public Boolean isAbsent()
    {
        return false;
    }

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
