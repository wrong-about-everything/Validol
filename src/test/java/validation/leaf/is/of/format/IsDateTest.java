package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.is.of.format.date.IsDate;
import validation.result.Unnamed;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.text.SimpleDateFormat;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsDateTest
{
    @Test
    public void nonSuccessfulPreviousResult() throws Exception
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
    public void successfulWithAbsentField() throws Exception
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
    public void isDate() throws Exception
    {
        IsDate named =
            new IsDate(
                () -> new Unnamed<>(Either.right(new Present<>("2019-05-25 08:07:54"))),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("2019-05-25 08:07:54", named.result().value().raw());
    }

    @Test
    public void isNotADate() throws Exception
    {
        IsDate named =
            new IsDate(
                () -> new Unnamed<>(Either.right(new Present<>("hello vasya"))),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a date of a certain format.", named.result().error());
    }
}
