package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.lessthan.genericvalue.IsLessThan;
import validation.result.value.Absent;
import validation.result.value.Present;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsLessThanTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsLessThan<?> named =
            new IsLessThan<>(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                ),
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsLessThan<?> named =
            new IsLessThan<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("lessValues")
    public <T extends Comparable<T>> void successfulWithPresentValues(T compared, T against) throws Exception
    {
        IsLessThan<T> named =
            new IsLessThan<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
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
    public <T extends Comparable<T>> void failedWithPresentValues(T compared, T against) throws Exception
    {
        IsLessThan<T> named =
            new IsLessThan<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
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
