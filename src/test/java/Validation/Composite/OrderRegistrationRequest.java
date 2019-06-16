package Validation.Composite;

class OrderRegistrationRequest
{
    private Guest guest;
//    private Bag bag;
//    private Delivery delivery;

    public OrderRegistrationRequest(
        Guest guest
//        ,
//        Bag bag,
//        Delivery delivery
    )
    {
        this.guest = guest;
//        this.bag = bag;
//        this.delivery = delivery;
    }

    public Guest guest()
    {
        return this.guest;
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
