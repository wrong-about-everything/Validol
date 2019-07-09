package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData;

import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.DeliveryData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.GuestData.GuestData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.ItemsData.ItemsData;

public class OrderRegistrationRequestData
{
    private GuestData guestData;
    private ItemsData itemsData;
    private DeliveryData deliveryData;
    private Integer source;

    public OrderRegistrationRequestData(
        GuestData guestData,
        ItemsData itemsData,
        DeliveryData deliveryData,
        Integer source
    )
    {
        this.guestData = guestData;
        this.itemsData = itemsData;
        this.deliveryData = deliveryData;
        this.source = source;
    }

    public GuestData guest()
    {
        return this.guestData;
    }

    public ItemsData items()
    {
        return this.itemsData;
    }

    public DeliveryData delivery()
    {
        return this.deliveryData;
    }

    public Integer source()
    {
        return this.source;
    }
}
