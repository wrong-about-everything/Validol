package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

public class WellFormedJson implements Validatable<String>
{
    private Validatable<String> original;

    public WellFormedJson(Validatable<String> validatable)
    {
        this.original = validatable;
    }

    public Result<String> result() throws Exception
    {
        Result<String> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return originalResult;
        }

        Gson gson = new Gson();
        try {
            gson.fromJson(originalResult.value(), Object.class);
            return new Named<>(originalResult.name(), Either.right(originalResult.value()));
        } catch(JsonSyntaxException ex) {
            return new Named<>(originalResult.name(), Either.left("This is an invalid json"));
        }
    }
}
