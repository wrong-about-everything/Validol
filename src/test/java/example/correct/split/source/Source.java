package example.correct.split.source;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.IndexedValue;
import validation.leaf.is.required.Required;
import validation.leaf.as.type.AsInteger;
import validation.result.Result;

final public class Source implements Validatable<Integer>
{
    private Validatable<Integer> v;

    public Source(JsonElement requestJsonObject) throws Exception
    {
        this.v =
            new AsInteger(
                new Required(
                    new IndexedValue("source", requestJsonObject)
                )
            );
    }

    @Override
    public Result<Integer> result() throws Exception
    {
        return this.v.result();
    }
}
