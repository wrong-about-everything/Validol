package validation.composite.operator.logical.xor;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.composite.ValidatableThrowingUncheckedException;
import validation.result.Named;
import validation.result.Result;
import validation.result.error.Error;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final public class OneOf implements Validatable
{
    private String fieldName;
    private Error error;
    private Validatable<?>[] validatables;

    public OneOf(String fieldName, Error error, Validatable<?> ... validatables) throws Exception
    {
        if (fieldName == null) {
            throw new Exception("Field name that is used to display error is required");
        }
        if (error == null) {
            throw new Exception("Error is required");
        }
        if (validatables == null) {
            throw new Exception("At least one validatable is required");
        }

        this.fieldName = fieldName;
        this.error = error;
        this.validatables = validatables;
    }

    public OneOf(String fieldName, Validatable<?> ... validatables) throws Exception
    {
        this(fieldName, new ThereMustBeSingleSuccessfulElement("There must be single successful element"), validatables);
    }

    public Result<?> result() throws Exception
    {
        try {
            List<Result<?>> successfulResults =
                Arrays.stream(this.validatables)
                    .map(
                        validatable -> new ValidatableThrowingUncheckedException<>(validatable).result()
                    )
                    .filter(result -> result.isSuccessful())
                    .collect(Collectors.toList());

            if (successfulResults.size() == 1) {
                return successfulResults.get(0);
            }
        } catch (RuntimeException e) {
            throw new Exception(e);
        }

        return new Named<>(this.fieldName, Either.left(this.error));
    }
}
