package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.value.lessthanorequal.genericvalue.IsLessThanOrEqual;
import validation.result.value.Absent;
import validation.result.value.Present;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsLessThanOrEqualTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsLessThanOrEqual<?> named =
            new IsLessThanOrEqual<>(
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
        IsLessThanOrEqual<?> named =
            new IsLessThanOrEqual<>(
                new NamedStub<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("lessOrEqualValues")
    public <T extends Comparable<T>> void successfulWithPresentValues(T compared, T against) throws Exception
    {
        IsLessThanOrEqual<T> named =
            new IsLessThanOrEqual<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(compared, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] lessOrEqualValues()
    {
        return
            new Object[][] {
                {1, 2},
                {1, 1},
                {"a", "a"},
                {"a", "b"},
                {"fedya", "fedya"},
                {"fedya", "vasya"},
                {false, false},
                {false, true},
            };
    }

    @Test
    @UseDataProvider("greaterValues")
    public <T extends Comparable<T>> void failedWithPresentValues(T compared, T against) throws Exception
    {
        IsLessThanOrEqual<T> named =
            new IsLessThanOrEqual<>(
                new NamedStub<>("vasya", Either.right(new Present<>(compared))),
                against
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(String.format("This value must be less than or equal to %s.", against), named.result().error().value().get("message"));
    }

    @DataProvider
    public static Object[][] greaterValues()
    {
        return
            new Object[][] {
                {3, 2},
                {'c', 'b'},
                {"vasyaaaaa", "vasya"},
                {true, false},
            };
    }
}
