package validation.leaf.is.of.format;

import com.google.gson.Gson;
import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.IndexedValue;
import validation.result.Unnamed;
import validation.value.Absent;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsDateTest
{
    @Test
    public void nonSuccessfulPreviousResult() throws Throwable
    {
        IsDate named =
            new IsDate(
                () -> new Unnamed<>(Either.left("hey there")),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("hey there", named.result().error());
    }

    @Test
    public void successfulWithAbsentField() throws Throwable
    {
        IsDate named =
            new IsDate(
                () -> new Unnamed<>(Either.right(new Absent<>())),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void isDate() throws Throwable
    {
        IsDate named =
            new IsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "2019-05-25 08:07:54")
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("2019-05-25 08:07:54", named.result().value().raw().getAsString());
    }

    @Test
    public void isNotADate() throws Throwable
    {
        IsDate named =
            new IsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "hello vasya")
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a date of a certain format.", named.result().error());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        IsDate named =
            new IsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a date of a certain format.", named.result().error());
    }
}
