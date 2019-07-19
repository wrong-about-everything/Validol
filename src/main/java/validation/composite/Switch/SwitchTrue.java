package validation.composite.Switch;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.spencerwi.either.Either;

import java.util.List;

final public class SwitchTrue<T> implements Validatable<T>
{
    private String name;
    private List<Case<T>> cases;

    public SwitchTrue(String name, List<Case<T>> cases)
    {
        this.name = name;
        this.cases = cases;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> result = this.caseResult();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }

    private Result<T> caseResult() throws Throwable
    {
        for (Case<T> _case : this.cases) {
            if (_case.isSatisfied()) {
                return _case.result();
            }
        }

        throw new Exception("None of the cases are satisfied. Specify a default one to prevent this exception from happening again,");
    }
}
