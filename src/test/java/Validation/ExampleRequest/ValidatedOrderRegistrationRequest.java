package Validation.ExampleRequest;

import Validation.Composite.*;
import Validation.Leaf.*;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;

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
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new NamedBloc<>(
                        "parsed request body",
                        List.of(
                            new FastFail<>(
                                new IsMap(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                    new NamedBloc<>(
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
//                                new AsList(
//                                    new IsArrayOfArrays(
                                new Required(
                                    new IndexedValue("items", requestJsonObject)
                                )
//                                    )
//                                )
                                ,
                                itemsJsonElement ->
                                    new NamedBloc<>(
                                        "items",
                                        List.of(
                                            new Mapped<>(
                                                itemsJsonElement,
                                                item ->
                                                    new UnnamedBloc<>(
                                                        List.of(
                                                            new AsInteger(
                                                                new Required(
                                                                    new IndexedValue("id", item)
                                                                )
                                                            )
                                                        ),
                                                        Item.class
                                                    )
                                            )
                                        ),
                                        Items.class
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
