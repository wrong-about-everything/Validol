package validation.Integration.FullFledgedJsonRequestExample;

import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.CourierDeliveryData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhenData.DefaultWhenData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.DeliveryData.CourierDeliveryData.WhereData.WhereData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.GuestData.GuestData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.ItemsData.ItemData.ItemData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.ItemsData.ItemsData;
import validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.OrderRegistrationRequestData;
import validation.composite.*;
import validation.composite.Switch.Specific;
import validation.composite.Switch.SwitchTrue;
import validation.composite.bloc.ofNameds.NamedBlocOfNameds;
import validation.composite.bloc.ofNameds.UnnamedBlocOfNameds;
import validation.composite.bloc.ofUnnameds.NamedBlocOfUnnameds;
import validation.leaf.as.AsDate;
import validation.leaf.as.AsInteger;
import validation.leaf.as.AsString;
import validation.leaf.IndexedValue;
import validation.leaf.is.IsInteger;
import validation.leaf.is.IsJsonObject;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.spencerwi.either.Either;
import validation.leaf.Named;
import validation.leaf.Required;

import java.text.SimpleDateFormat;
import java.util.List;

// TODO: 7/1/19 Create another class representing the same validatable request, but with nested blocks. That would be a more concise version with improved readability.
public class ValidatedOrderRegistrationRequest implements Validatable<OrderRegistrationRequestData>
{
    private String jsonRequestString;

    public ValidatedOrderRegistrationRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<OrderRegistrationRequestData> result() throws Throwable
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
                                        // @todo One can get an info about the class name from type parameter.
                                        GuestData.class
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
                                                ItemData.class
                                            ),
                                        ItemsData.class
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
                                                                    WhereData.class
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
                                                    CourierDeliveryData.class
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
