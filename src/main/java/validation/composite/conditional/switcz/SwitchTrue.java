package validation.composite.conditional.switcz;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.spencerwi.either.Either;

import java.util.List;

final public class SwitchTrue<T> implements Validatable<T>
{
    private String name;
    private List<Case<T>> cases;

    public SwitchTrue(String name, List<Case<T>> cases) throws Exception
    {
        if (name == null) {
            throw new Exception("Name can not be null");
        }
        if (cases == null) {
            throw new Exception("Cases list can not be null");
        }

        this.name = name;
        this.cases = cases;
    }

    public Result<T> result() throws Exception
    {
        Result<T> result = this.caseResult();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }

    private Result<T> caseResult() throws Exception
    {
        for (Case<T> _case : this.cases) {
            if (_case.isSatisfied()) {
                return _case.result();
            }
        }

        throw new Exception("None of the cases are satisfied. Specify a default one to prevent this exception from happening again,");
    }
}
