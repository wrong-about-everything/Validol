package validation.composite.operator.logical.or;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Result;
import validation.result.Unnamed;
import validation.result.value.Absent;

final public class Or implements Validatable<Boolean>
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
                        new EitherLeftOrRight(
                            leftResult.error().value().get("message").toString(),
                            rightResult.error().value().get("message").toString()
                        )
                    )
                );
        }

        return new Unnamed<>(Either.right(new Absent<>()));
    }
}
