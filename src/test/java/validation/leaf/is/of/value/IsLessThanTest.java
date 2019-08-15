package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsLessThanTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsLessThan<?> named =
            new IsLessThan<>(
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
    public void successfulWithAbsentValue() throws Throwable
    {
        IsLessThan<?> named =
            new IsLessThan<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("lessValues")
    public <T extends Comparable<T>> void successfulWithPresentValues(T compared, T against) throws Throwable
    {
        IsLessThan<T> named =
            new IsLessThan<>(
                new Named<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(compared, named.result().value().raw());
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

    @Test
    @UseDataProvider("greaterOrEqualValues")
    public <T extends Comparable<T>> void failedWithPresentValues(T compared, T against) throws Throwable
    {
        IsLessThan<T> named =
            new IsLessThan<>(
                new Named<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(String.format("This value must be less than %s.", against), named.result().error());
    }

    @DataProvider
    public static Object[][] greaterOrEqualValues()
    {
        return
            new Object[][] {
                {2, 2},
                {3, 2},
                {'b', 'b'},
                {'c', 'b'},
                {"vasya", "vasya"},
                {"vasyaaaaa", "vasya"},
                {true, true},
                {true, false},
            };
    }
}
