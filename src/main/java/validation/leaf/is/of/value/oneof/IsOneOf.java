package validation.leaf.is.of.value.oneof;

import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import java.util.List;

final public class IsOneOf<T> implements Validatable<T>
{
    private Validatable<T> original;
    private List<T> list;

    public IsOneOf(Validatable<T> original, List<T> list) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (list == null) {
            throw new Exception("List to check against can not be null");
        }

        this.original = original;
        this.list = list;
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.list.contains(prevResult.value().raw())) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeOneOf<>(this.list));
        }

        return prevResult;
    }
}
