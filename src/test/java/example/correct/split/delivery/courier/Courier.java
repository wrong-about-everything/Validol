package example.correct.split.delivery.courier;

import com.google.gson.JsonElement;
import example.correct.bag.delivery.Delivery;
import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhenData;
import example.correct.bag.delivery.courier.where.Where;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.leaf.as.AsDate;
import validation.leaf.as.AsInteger;
import validation.leaf.as.AsString;
import validation.result.Result;

import java.text.SimpleDateFormat;
import java.util.List;

public class Courier implements Validatable<Delivery>
{
    private Validatable<Delivery> v;

    public Courier(JsonElement deliveryJsonElement)
    {
        this.v =
            new UnnamedBlocOfNameds<>(
                List.of(
                    new FastFail<>(
                        new IndexedValue("where", deliveryJsonElement),
                        whereJsonElement ->
                            new NamedBlocOfNameds<>(
                                "where",
                                List.of(
                                    new AsString(
                                        new Required(
                                            new IndexedValue("street", whereJsonElement)
                                        )
                                    ),
                                    new AsInteger(
                                        new Required(
                                            new IndexedValue("building", whereJsonElement)
                                        )
                                    )
                                ),
                                Where.class
                            )
                    ),
                    new FastFail<>(
                        new IndexedValue("when", deliveryJsonElement),
                        whenJsonElement ->
                            new NamedBlocOfNameds<>(
                                "when",
                                List.of(
                                    new AsDate(
                                        new Required(
                                            new IndexedValue("date", whenJsonElement)
                                        ),
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    )
                                ),
                                DefaultWhenData.class
                            )
                    )
                ),
                CourierDelivery.class
            );
    }

    @Override
    public Result<Delivery> result() throws Throwable
    {
        return this.v.result();
    }
}
