package validation.composite.bloc.of.unnamed;

import com.spencerwi.either.Either;
import org.javatuples.Pair;
import org.junit.Test;
import validation.ErrorStub;
import validation.composite.bloc.of.unnameds.ValuesAndErrorsOfUnnameds;
import validation.composite.bloc.of.unnamed.bag.Integers;
import validation.leaf.Unnamed;
import validation.result.value.Present;
import java.util.List;

import static org.junit.Assert.*;

final public class ValuesAndErrorsOfUnnamedsTest
{
    @Test
    public void noErrorsInPlainUnnameds() throws Exception
    {
        Pair<List<Object>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds<Object>(
                List.of(
                    new Unnamed<>(
                        Either.right(
                            new Present<>("vasya")
                        )
                    ),
                    new Unnamed<>(
                        Either.right(
                            new Present<>(123)
                        )
                    ),
                    new Unnamed<>(
                        Either.right(
                            new Present<>(true)
                        )
                    )
                )
            )
                .value();

        assertEquals(List.of("vasya", 123, true), valuesAndErrors.getValue0());
        assertEquals(List.of(), valuesAndErrors.getValue1());
    }

    @Test
    public void failedValidationOfPlainUnnameds() throws Exception
    {
        Pair<List<String>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds<>(
                List.of(
                    new Unnamed<>(
                        Either.right(
                            new Present<>("ok")
                        )
                    ),
                    new Unnamed<>(
                        Either.left(
                            new ErrorStub("error 2")
                        )
                    )
                )
            )
                .value();

        assertEquals(List.of(), valuesAndErrors.getValue0());
        assertEquals(new ErrorStub("error 2").value(), valuesAndErrors.getValue1().get(0));
    }

    @Test
    public void successfulValidationOfUnnamedBlocks() throws Exception
    {
        Integers i = new Integers(List.of(1, 2, 3));

        Pair<List<Integers>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds<Integers>(
                List.of(
                    () -> new validation.result.Unnamed<>(Either.right(new Present<>(i)))
                )
            )
                .value();

        assertEquals(i, valuesAndErrors.getValue0().get(0));
        assertEquals(List.of(), valuesAndErrors.getValue1());
    }
}
