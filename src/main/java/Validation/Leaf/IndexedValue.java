package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
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
            // @todo Introduce JsonElement null object somehow. Returning JsonNull is semantically wrong
            return new Named<>(this.name, Either.right(JsonNull.INSTANCE));
        }

        if (!this.json.getAsJsonObject().has(this.name)) {
            return new Named<>(this.name, Either.right(JsonNull.INSTANCE));
        }

        return new Named<>(this.name, Either.right(this.json.getAsJsonObject().get(this.name)));
    }
}
