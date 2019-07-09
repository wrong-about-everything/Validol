package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData;

import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhenData.WhenData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhereData.WhereData;

public interface DeliveryData
{
    public Boolean isByCourier();

    public WhereData where() throws Throwable;

    public WhenData when() throws Throwable;
}
