package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Absent;

public class And implements Validatable<Boolean>
{
    private Validatable<Boolean> left;
    private Validatable<Boolean> right;

    public And(Validatable<Boolean> left, Validatable<Boolean> right) throws Exception
    {
        if (left == null) {
            throw new Exception("Left validatable can not be null");
        }
        if (right == null) {
            throw new Exception("Right validatable can not be null");
        }

        this.left = left;
        this.right = right;
    }

    public Result<Boolean> result() throws Throwable
    {
        Result<Boolean> leftResult = this.left.result();
        Result<Boolean> rightResult = this.right.result();

        if (!leftResult.isSuccessful()) {
            return leftResult;
        }
        if (!rightResult.isSuccessful()) {
            return rightResult;
        }

        return new Unnamed<>(Either.right(new Absent<>()));
    }
}
