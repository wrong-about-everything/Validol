package example.correct.inline;

import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhen;
import example.correct.bag.delivery.courier.where.Where;
import example.correct.bag.guest.Guest;
import example.correct.bag.items.item.Item;
import example.correct.bag.items.Items;
import example.correct.bag.OrderRegistrationRequestData;
import validation.composite.*;
import validation.composite.conditional.switcz.Specific;
import validation.composite.conditional.switcz.SwitchTrue;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.bloc.of.unnameds.NamedBlocOfUnnameds;
import validation.leaf.Unnamed;
import validation.leaf.as.format.AsDate;
import validation.leaf.as.type.AsInteger;
import validation.leaf.as.type.AsString;
import validation.leaf.IndexedValue;
import validation.leaf.is.of.structure.IsJsonObject;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.spencerwi.either.Either;
import validation.leaf.Required;
import java.text.SimpleDateFormat;
import java.util.List;

public class ValidatedOrderRegistrationRequest implements Validatable<OrderRegistrationRequestData>
{
    private String jsonRequestString;

    public ValidatedOrderRegistrationRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<OrderRegistrationRequestData> result() throws Exception
    {
        // todo: Обработать случаи, когда классы используются неверно. Например, NamedBlocOfNameds используется с неименованными элементами валидации.
        return
            new FastFail<>(
                new WellFormedJson(
                    new Unnamed<>(Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new FastFail<>(
                                new IsJsonObject(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                    new NamedBlocOfNameds<>(
                                        "guest",
                                        List.of(
                                            new AsString(
                                                new Required(
                                                    new IndexedValue("email", guestJsonObject)
                                                )
                                            ),
                                            new AsString(
                                                new Required(
                                                    new IndexedValue("name", guestJsonObject)
                                                )
                                            )
                                        ),
                                        Guest.class
                                    )
                            ),
                            new FastFail<>(
                                new Required(
                                    new IndexedValue("items", requestJsonObject)
                                )
                                ,
                                itemsJsonElement ->
                                    new NamedBlocOfUnnameds<>(
                                        "items",
                                        itemsJsonElement,
                                        item ->
                                            new UnnamedBlocOfNameds<>(
                                                List.of(
                                                    new AsInteger(
                                                        new Required(
                                                            new IndexedValue("id", item)
                                                        )
                                                    )
                                                ),
                                                Item.class
                                            ),
                                        Items.class
                                    )
                            ),
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
                                                )
                                            )
                                        )
                                    )
                            ),
                            new AsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequestData.class
                    )
            )
                .result()
            ;
    }
}
