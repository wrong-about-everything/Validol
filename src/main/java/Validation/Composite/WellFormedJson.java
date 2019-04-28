package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;

public class WellFormedJson implements Validatable
{
    private Validatable<String> original;

    public WellFormedJson(Validatable<String> validatable)
    {
        this.original = validatable;
    }

    public Result<String> result()
    {
        Result<String> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return originalResult;
        }

        return new Named<>(originalResult.name(), originalResult.value(), true);
    }
}
