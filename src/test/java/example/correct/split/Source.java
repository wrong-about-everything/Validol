package example.correct.split;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.IndexedValue;
import validation.leaf.Required;
import validation.leaf.as.AsInteger;
import validation.result.Result;

public class Source implements Validatable<Integer>
{
    private Validatable<Integer> v;

    public Source(JsonElement requestJsonObject)
    {
        this.v =
            new AsInteger(
                new Required(
                    new IndexedValue("source", requestJsonObject)
                )
            );
    }

    @Override
    public Result<Integer> result() throws Throwable
    {
        return this.v.result();
    }
}
