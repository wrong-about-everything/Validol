package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.Named;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class IsGreaterThanOrEqualTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsGreaterThanOrEqual<?> named =
            new IsGreaterThanOrEqual<>(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsGreaterThanOrEqual<?> named =
            new IsGreaterThanOrEqual<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("greaterThanOrEqualValues")
    public <T extends Comparable<T>> void successfulWithPresentValues(T compared, T against) throws Exception
    {
        IsGreaterThanOrEqual<T> named =
            new IsGreaterThanOrEqual<>(
                new Named<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(compared, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] greaterThanOrEqualValues()
    {
        return
            new Object[][] {
                {2, 1},
                {2, 2},
                {"b", "a"},
                {"vasya", "fedya"},
                {"vasya", "vasya"},
                {true, true},
                {true, false},
            };
    }

    @Test
    @UseDataProvider("lessValues")
    public <T extends Comparable<T>> void failedWithPresentValues(T compared, T against) throws Exception
    {
        IsGreaterThanOrEqual<T> named =
            new IsGreaterThanOrEqual<>(
                new Named<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(String.format("This value must be greater than or equal to %s.", against), named.result().error());
    }

    @DataProvider
    public static Object[][] lessValues()
    {
        return
            new Object[][] {
                {1, 2},
                {"a", "b"},
                {"fedya", "vasya"},
                {false, true},
            };
    }
}
