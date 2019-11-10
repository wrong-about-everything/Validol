package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Absent;

public class Or implements Validatable<Boolean>
{
    private Validatable<Boolean> left;
    private Validatable<Boolean> right;

    public Or(Validatable<Boolean> left, Validatable<Boolean> right) throws Exception
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

    public Result<Boolean> result() throws Exception
    {
        Result<Boolean> leftResult = this.left.result();
        Result<Boolean> rightResult = this.right.result();

        if (!leftResult.isSuccessful() && !rightResult.isSuccessful()) {
            return
                new Unnamed<>(
                    Either.left(
                        String.format(
                            "Either %s or %s",
                            leftResult.error(),
                            rightResult.error()
                        )
                    )
                );
        }

        return new Unnamed<>(Either.right(new Absent<>()));
    }
}
