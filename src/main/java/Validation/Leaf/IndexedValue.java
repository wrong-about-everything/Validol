package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Absent;
import Validation.Value.Present;
import Validation.Value.Value;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

public class IndexedValue implements Validatable<JsonElement>
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
