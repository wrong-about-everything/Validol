package validation.composite.bloc.of.callback;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.composite.FastFail;
import validation.composite.VFunction;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.result.Result;

final public class RequiredNamedBlocOfCallback<T> implements Validatable<T>
{
    private Validatable<T> v;

    public RequiredNamedBlocOfCallback(
        String name,
        JsonElement requestJsonObject,
        VFunction<JsonElement, Validatable<T>> returnedBloc
    ) throws Exception
    {
        this.v =
            new FastFail<>(
                new Required(
                    new IndexedValue(name, requestJsonObject)
                ),
                returnedBloc
            );
    }

    @Override
    public Result<T> result() throws Exception
    {
        return this.v.result();
    }
}
