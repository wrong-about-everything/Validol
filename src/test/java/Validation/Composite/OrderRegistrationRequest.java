package Validation.Composite;

class OrderRegistrationRequest
{
    private Guest guest;
    private Integer source;
//    private Bag bag;
//    private Delivery delivery;

    public OrderRegistrationRequest(
        Guest guest,
        Integer source
//        ,
//        Bag bag,
//        Delivery delivery
    )
    {
        this.guest = guest;
        this.source = source;
//        this.bag = bag;
//        this.delivery = delivery;
    }

    public Guest guest()
    {
        return this.guest;
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
