package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.ItemsData;

import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.ItemsData.ItemData.ItemData;

import java.util.List;

public class ItemsData
{
    private List<ItemData> list;

    public ItemsData(List<ItemData> list)
    {
        this.list = list;
    }

    public List<ItemData> list()
    {
        return this.list;
    }
}
