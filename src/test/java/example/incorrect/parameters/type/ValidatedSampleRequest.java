package example.incorrect.parameters.type;

import validation.leaf.as.type.AsString;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.of.type.integer.IsInteger;
import validation.leaf.is.NamedStub;
import validation.leaf.is.required.Required;
import validation.result.Result;
import validation.Validatable;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import validation.composite.FastFail;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.composite.wellformedjson.WellFormedJson;

import java.util.List;

final public class ValidatedSampleRequest implements Validatable<SampleRequest>
{
    private String jsonRequestString;

    public ValidatedSampleRequest(String jsonRequestString)
    {
        this.jsonRequestString = jsonRequestString;
    }

    @Override
    public Result<SampleRequest> result() throws Exception
    {
        return
            new FastFail<>(
                new WellFormedJson(
                    new NamedStub<>("parsed request body", Either.right(new Present<>(this.jsonRequestString)))
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
