package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.greaterthan.genericvalue.IsGreaterThan;
import validation.result.value.Absent;
import validation.result.value.Present;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsGreaterThanTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new NamedStub<>(
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
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("greaterValues")
    public <T extends Comparable<T>> void successfulWithPresentValues(T compared, T against) throws Exception
    {
        IsGreaterThan<T> named =
            new IsGreaterThan<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(compared, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] greaterValues()
    {
        return
            new Object[][] {
                {2, 1},
                {"b", "a"},
                {"vasya", "fedya"},
                {true, false},
            };
    }

    @Test
    @UseDataProvider("lessOrEqualValues")
    public <T extends Comparable<T>> void failedWithPresentValues(T compared, T against) throws Exception
    {
        IsGreaterThan<T> named =
            new IsGreaterThan<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(String.format("This value must be greater than %s.", against), named.result().error());
    }

    @DataProvider
    public static Object[][] lessOrEqualValues()
    {
        return
            new Object[][] {
                {2, 2},
                {2, 3},
                {'b', 'b'},
                {'b', 'c'},
                {"vasya", "vasya"},
                {"vasya", "vasyaaaaa"},
                {true, true},
                {false, true},
            };
    }
}
