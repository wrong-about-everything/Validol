package validation.leaf;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.value.Absent;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

// todo: https://symfony.com/doc/current/reference/constraints.html
final public class IndexedValue implements Validatable<JsonElement>
{
    private String name;
    private JsonElement json;

    public IndexedValue(String name, JsonElement json)
    {
        this.name = name;
        this.json = json;
    }

    public Result<JsonElement> result()
    {
        if (!this.json.isJsonObject()) {
            return new Named<>(this.name, Either.left("IndexedValue class is used on an inappropriate data structure"));
        }

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
