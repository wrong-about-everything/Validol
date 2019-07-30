package example.incorrect.parameters.type;

import validation.leaf.as.AsString;
import validation.leaf.IndexedValue;
import validation.leaf.is.of.type.IsInteger;
import validation.leaf.Named;
import validation.leaf.Required;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.spencerwi.either.Either;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.WellFormedJson;

import java.util.List;

public class ValidatedSampleRequest implements Validatable<SampleRequest>
{
    private String jsonRequestString;

    public ValidatedSampleRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<SampleRequest> result() throws Throwable
    {
        return
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
                ),
                requestJsonObject ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new IsInteger(
                                new Required(
                                    new IndexedValue("key1", requestJsonObject)
                                )
                            ),
                            new AsString(
                                new Required(
                                    new IndexedValue("key2", requestJsonObject)
                                )
                            )
                        ),
                        SampleRequest.class
                    )
            )
                .result()
            ;
    }
}
