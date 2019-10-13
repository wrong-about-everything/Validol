package example.correct.bag;

import example.correct.bag.guest.Guest;
import example.correct.bag.items.Items;

public class OrderRegistrationRequestData
{
    private Guest guest;
    private Items items;
    private Delivery delivery;
    private Integer source;

    public OrderRegistrationRequestData(
        Guest guest,
        Items items,
        Delivery delivery,
        Integer source
    )
    {
        this.guest = guest;
        this.items = items;
        this.delivery = delivery;
        this.source = source;
    }

    public Guest guest()
    {
        return this.guest;
    }

    public Items items()
    {
        return this.items;
    }

    public Delivery delivery()
    {
        return this.delivery;
    }

    public Integer source()
    {
        return this.source;
    }
}
