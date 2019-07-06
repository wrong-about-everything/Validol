package Validation.ExampleRequest;

import Validation.Composite.*;
import Validation.Leaf.*;
import Validation.Leaf.As.AsDate;
import Validation.Leaf.As.AsInteger;
import Validation.Leaf.As.AsString;
import Validation.Leaf.Is.IsDate;
import Validation.Leaf.Is.IsInteger;
import Validation.Leaf.Is.IsMap;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;

import java.text.SimpleDateFormat;
import java.util.List;

// TODO: 7/1/19 Create another class representing the same validatable request, but with nested blocks. That would be a more concise version with improved readbility.
public class ValidatedOrderRegistrationRequest implements Validatable<OrderRegistrationRequest>
{
    private String jsonRequestString;

    public ValidatedOrderRegistrationRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<OrderRegistrationRequest> result() throws Throwable
    {
        // todo: Обработать случаи, когда классы используются неверно. Например, NamedBlocOfNameds используется с неименованными элементами валидации.
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new NamedBlocOfNameds<>(
                        "parsed request body",
                        List.of(
                            new FastFail<>(
                                new IsMap(
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
                                        // @todo One can get an info about the class name from type parameter.
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
                            // TODO: Подумать как спрятать FastFail, чтобы сделать код более кратким
                            // TODO: Подумать как обработать кейс, когда в FastFail блок необязательный. Например, когда delivery необязательный.
                            new FastFail<>(
                                new Required(
                                    new IndexedValue("delivery", requestJsonObject)
                                ),
                                deliveryJsonElement ->
                                    new SwitchTrue<>(
                                        "delivery",
                                        List.of(
                                            new Case<>(
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
                                                                        new IsInteger(
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
                            new IsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequest.class
                    )
            )
                .result()
            ;
    }
}
