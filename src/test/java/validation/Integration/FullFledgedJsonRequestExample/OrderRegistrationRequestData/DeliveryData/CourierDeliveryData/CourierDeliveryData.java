package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData;

import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhenData.EmptyWhenData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhenData.WhenData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.DeliveryData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhereData.WhereData;

public class CourierDeliveryData implements DeliveryData
{
    private WhereData whereData;
    private WhenData whenData;

    public CourierDeliveryData(WhereData whereData, WhenData whenData)
    {
        this.whereData = whereData;
        this.whenData = whenData;
    }

    public CourierDeliveryData(WhereData whereData)
    {
        this(whereData, new EmptyWhenData());
    }

    @Override
    public Boolean isByCourier()
    {
        return true;
    }

    @Override
    public WhereData where()
    {
        return this.whereData;
    }

    @Override
    public WhenData when()
    {
        return this.whenData;
    }
}
