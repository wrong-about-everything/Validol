package validation.composite.conditional.switcz;

import validation.ErrorStub;
import validation.composite.bloc.of.nameds.UnnamedBlocOfNameds;
import validation.leaf.Unnamed;
import validation.result.Named;
import validation.result.Result;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

final public class SwitchTrueTest
{
    @Test
    public void firstCaseIsSatisfiedAndIsSuccessful() throws Exception
    {
        Result<Team> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Specific<>(
                        () -> true,
                        new UnnamedBlocOfNameds<>(
                            List.of(
                                () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                                () -> new Named<>("fedya", Either.right(new Present<>(7))),
                                () -> new Named<>(
                                    "tolya",
                                    Either.right(
                                        new Present<>(
                                            Map.of("id", 245)
                                        )
                                    )
                                ),
                                () -> new Named<>("jenya", Either.right(new Present<>(false)))
                            ),
                            Team.class
                        )
                    )
                )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("belov", result.value().raw().vasya());
        assertEquals(Integer.valueOf(7), result.value().raw().fedya());
        assertEquals(Map.of("id", 245), result.value().raw().tolya());
        assertEquals(false, result.value().raw().jenya());
    }

    @Test
    public void secondCaseIsSatisfiedAndIsSuccessful() throws Exception
    {
        Result<String> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Specific<>(
                        () -> false,
                        new Unnamed<>(Either.right(new Present<>("hello")))
                    ),
                    new Specific<>(
                        () -> true,
                        new Unnamed<>(Either.right(new Present<>("world")))
                    )
                )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("world", result.value().raw());
    }

    @Test
    public void noneOfTheCasesAreSatisfiedAndDefaultCaseIsAbsent() throws Exception
    {
        try {
            new SwitchTrue<>(
                "team",
                List.of(
                    new Specific<>(
                        () -> false,
                        new Unnamed<>(Either.right(new Present<>("hello")))
                    ),
                    new Specific<>(
                        () -> false,
                        new Unnamed<>(Either.right(new Present<>("world")))
                    )
                )
            )
                .result();
        } catch (Throwable e) {
            assertTrue(true);
            return;
        }

        fail("SwitchTrue with no satisfied cases requires a DefaultCase");
    }

    @Test
    public void noneOfTheCasesAreSatisfiedAndDefaultCaseIsPresent() throws Exception
    {
        Result<String> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Specific<>(
                        () -> false,
                        new Unnamed<>(Either.right(new Present<>("hello")))
                    ),
                    new Specific<>(
                        () -> false,
                        new Unnamed<>(Either.right(new Present<>("world")))
                    ),
                    new Default<>(
                        new Unnamed<>(Either.right(new Present<>("!")))
                    )
                )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("!", result.value().raw());
    }

    @Test
    public void someFieldsFailed() throws Exception
    {
        Result<Team> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Specific<>(
                        () -> true,
                        new UnnamedBlocOfNameds<>(
                            List.of(
                                () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                                () -> new Named<>("fedya", Either.left(new ErrorStub("Ooops"))),
                                () -> new Named<>("tolya", Either.left(new ErrorStub("Woooooooops"))),
                                () -> new Named<>("jenya", Either.right(new Present<>(false)))
                            ),
                            Team.class
                        )
                    )
                )
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            Map.of(
                "fedya", new ErrorStub("Ooops").value(),
                "tolya", new ErrorStub("Woooooooops").value()
            ),
            result.error().value()
        );
    }
}
