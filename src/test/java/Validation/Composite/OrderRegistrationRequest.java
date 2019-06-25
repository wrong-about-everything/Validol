package Validation.Composite;

class OrderRegistrationRequest
{
    private Guest guest;
    private Items items;
    private Integer source;
//    private Bag bag;
//    private Delivery delivery;

    public OrderRegistrationRequest(
        Guest guest,
        Items items,
        Integer source
//        ,
//        Bag bag,
//        Delivery delivery
    )
    {
        this.guest = guest;
        this.items = items;
        this.source = source;
//        this.bag = bag;
//        this.delivery = delivery;
    }

    public Guest guest()
    {
        return this.guest;
    }

    public Items items()
    {
        return this.items;
    }

    public Integer source()
    {
        return this.source;
    }

//    public Bag bag()
//    {
//        return this.bag;
//    }
//
//    public Delivery delivery()
//    {
//        return this.delivery;
//    }
}
