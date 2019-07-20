package example.correct.split;

import com.google.gson.JsonElement;
import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhenData;
import example.correct.bag.delivery.courier.where.Where;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.switcz.Specific;
import validation.composite.switcz.SwitchTrue;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.leaf.as.AsDate;
import validation.leaf.as.AsInteger;
import validation.leaf.as.AsString;
import validation.result.Result;

import java.text.SimpleDateFormat;
import java.util.List;

public class Delivery implements Validatable<example.correct.bag.delivery.Delivery>
{
    private Validatable<example.correct.bag.delivery.Delivery> v;

    public Delivery(JsonElement requestJsonObject)
    {
        this.v =
            new FastFail<>(
                new Required(
                    new IndexedValue("delivery", requestJsonObject)
                ),
                deliveryJsonElement ->
                    new SwitchTrue<>(
                        "delivery",
                        List.of(
                            new Specific<>(
                                () -> true,
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
                                )
                            )
                        )
                    )
            );
    }

    @Override
    public Result<example.correct.bag.delivery.Delivery> result() throws Throwable
    {
        return this.v.result();
    }
}
