package validation.result;

import com.spencerwi.either.Either;
import validation.value.Absent;
import validation.value.Value;

final public class AbsentField<T, R> implements Result<R>
{
    private Result<R> result;

    public AbsentField(Result<T> result) throws Throwable
    {
        if (!result.isSuccessful()) {
            throw new Exception("Result must be successful");
        }
        if (result.value().isPresent()) {
            throw new Exception("Value must be absent");
        }

        this.result =
            result.isNamed()
                ?
                    new Named<>(
                        result.name(),
                        Either.right(
                            new Absent<>()
                        )
                    )
                :
                    new Unnamed<>(
                        Either.right(
                            new Absent<>()
                        )
                    )
        ;
    }

    @Override
    public Boolean isSuccessful()
    {
        return true;
    }

    @Override
    public Value<R> value() throws Exception
    {
        return this.result.value();
    }

    @Override
    public Object error() throws Exception
    {
        throw new Exception("No error exists in successful result");
    }

    @Override
    public Boolean isNamed()
    {
        return this.result.isNamed();
    }

    @Override
    public String name() throws Exception
    {
        if (!this.isNamed()) {
            throw new Exception("Unnamed result does not have a name");
        }

        return this.result.name();
    }
}
