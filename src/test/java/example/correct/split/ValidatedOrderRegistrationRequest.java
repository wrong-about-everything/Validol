package example.correct.split;

import com.spencerwi.either.Either;
import example.correct.bag.OrderRegistrationRequestData;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.WellFormedJson;
import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.leaf.Named;
import validation.result.Result;
import validation.value.Present;
import java.util.List;

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
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new NamedBlocOfNameds<>(
                        "parsed request body",
                        List.of(
                            new Guest(requestJsonObject),
                            new Items(requestJsonObject),
                            new Delivery(requestJsonObject),
                            new Source(requestJsonObject)
                        ),
                        OrderRegistrationRequestData.class
                    )
            )
                .result()
            ;
    }
}
