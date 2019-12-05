package validation.leaf.is;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.result.value.Absent;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class IndexedValue implements Validatable<JsonElement>
{
    private String name;
    private JsonElement json;

    public IndexedValue(String name, JsonElement json) throws Exception
    {
        if (name == null) {
            throw new Exception("Name can not be null");
        }
        if (json == null) {
            throw new Exception("Json can not be null");
        }

        this.name = name;
        this.json = json;
    }

    public Result<JsonElement> result() throws Exception
    {
        if (!this.json.getAsJsonObject().has(this.name)) {
            return new Named<>(this.name, Either.right(new Absent<>()));
        }

        return
            new Named<>(
                this.name,
                Either.right(
                    new Present<>(
                        this.json.getAsJsonObject().get(this.name)
                    )
                )
            );
    }
}
