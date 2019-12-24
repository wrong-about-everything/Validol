package example.correct.split.delivery.courier;

import com.google.gson.JsonElement;
import example.correct.bag.Delivery;
import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhen;
import example.correct.bag.delivery.courier.where.Where;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.leaf.as.format.AsDate;
import validation.leaf.as.type.AsInteger;
import validation.leaf.as.type.AsString;
import validation.result.Result;

import java.text.SimpleDateFormat;
import java.util.List;

final public class Courier implements Validatable<Delivery>
{
    private Validatable<Delivery> v;

    public Courier(JsonElement deliveryJsonElement) throws Exception
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
                                        new AsString(
                                            new Required(
                                                new IndexedValue("date", whenJsonElement)
                                            )
                                        ),
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    )
                                ),
                                DefaultWhen.class
                            )
                    )
                ),
                CourierDelivery.class
            );
    }

    @Override
    public Result<Delivery> result() throws Exception
    {
        return this.v.result();
    }
}
