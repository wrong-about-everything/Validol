package validation.composite.operator.logical.or;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.composite.ValidatableThrowingUncheckedException;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.result.error.Error;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final public class AtLeastOneIsSuccessful implements Validatable
{
    private String fieldName;
    private Error error;
    private Validatable<?>[] validatables;

    public AtLeastOneIsSuccessful(String fieldName, Error error, Validatable<?> ... validatables) throws Exception
    {
        if (fieldName == null) {
            throw new Exception("Field name to bind an error to is required");
        }
        if (error == null) {
            throw new Exception("Right validatable can not be null");
        }
        if (validatables == null) {
            throw new Exception("At least one validatable element is required");
        }

        this.fieldName = fieldName;
        this.error = error;
        this.validatables = validatables;
    }

    public AtLeastOneIsSuccessful(String fieldName, Validatable<?> ... validatables) throws Exception
    {
        this(fieldName, new AtLeastOneMustBeSuccessful("At least one of the fields must be successful"), validatables);
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

            if (!successfulResults.isEmpty()) {
                return
                    new Unnamed<>(
                        Either.right(
                            new Present<>(
                                successfulResults.stream()
                                    .filter(
                                        result -> {
                                            try {
                                                return result.value().isPresent();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    )
                                    .map(
                                        result -> {
                                            try {
                                                return result.value().raw();
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    )
                                    .collect(Collectors.toList())
                            )
                        )
                    );
            }
        } catch (RuntimeException e) {
            throw new Exception(e);
        }

        return new Named<>(this.fieldName, Either.left(this.error));
    }
}
