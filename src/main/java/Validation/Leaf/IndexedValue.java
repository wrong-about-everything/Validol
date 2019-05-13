package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spencerwi.either.Either;

public class IndexedValue implements Validatable<JsonElement>
{
    private String name;
    private JsonObject json;

    public IndexedValue(String name, JsonObject json)
    {
        this.name = name;
        this.json = json;
    }

    public Result<JsonElement> result()
    {
        if (!this.json.has(this.name)) {
            return new Named<>(this.name, Either.left(String.format("Key %s does not exist", this.name)));
        }

        return new Named<>(this.name, Either.right(this.json.get(this.name)));
    }
}
