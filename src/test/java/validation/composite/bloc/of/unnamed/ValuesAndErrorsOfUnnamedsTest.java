package validation.composite.bloc.of.unnamed;

import com.spencerwi.either.Either;
import org.javatuples.Pair;
import org.junit.Test;
import validation.composite.bloc.of.unnameds.ValuesAndErrorsOfUnnameds;
import validation.composite.bloc.of.unnamed.bag.Integers;
import validation.leaf.Unnamed;
import validation.result.value.Present;
import java.util.List;

import static org.junit.Assert.*;

// TODO: reach 100% test coverage
public class ValuesAndErrorsOfUnnamedsTest
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
                            "error 2"
                        )
                    )
                )
            )
                .value();

        assertEquals(List.of(), valuesAndErrors.getValue0());
        assertEquals(List.of("error 2"), valuesAndErrors.getValue1());
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
