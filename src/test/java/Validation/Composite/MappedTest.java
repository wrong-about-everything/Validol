package Validation.Composite;

import Validation.Leaf.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class MappedTest
{
    @Test
    public void success() throws Throwable
    {
        Result<List<Validatable<Integer>>> result =
            new Mapped<>(
                List.of("string element"),
                stringElement ->
                    () ->
                        new Validation.Result.Unnamed<>(
                            Either.right(
                                new Present<>(stringElement.length())
                            )
                        )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(Integer.valueOf(14), result.value().raw().get(0).result().value().raw());
    }

    @Test
    public void fail() throws Throwable
    {
        Result<List<Validatable<Integer>>> result =
            new Mapped<>(
                List.of("string element"),
                stringElement ->
                    new Validatable<Integer>() {
                        @Override
                        public Result<Integer> result() throws Exception
                        {
                            return new Validation.Result.Unnamed<>(Either.left("Ooooooops"));
                        }
                    }
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("Ooooooops", result.value().raw().get(0).result().error());
    }
}
