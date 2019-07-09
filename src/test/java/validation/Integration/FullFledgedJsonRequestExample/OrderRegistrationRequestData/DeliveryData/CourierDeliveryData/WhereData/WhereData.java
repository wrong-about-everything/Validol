package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhereData;

public class WhereData
{
    private String street;
    private Integer building;

    public WhereData(String street, Integer building)
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
