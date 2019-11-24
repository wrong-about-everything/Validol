package validation.leaf.as.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.result.Unnamed;
import validation.value.Absent;
import validation.value.Present;
import java.text.SimpleDateFormat;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class AsDateTest
{
    @Test
    public void nonSuccessfulPreviousResult() throws Exception
    {
        AsDate named =
            new AsDate(
                () -> new Unnamed<>(Either.left("hey there")),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("hey there", named.result().error());
    }

    @Test
    public void nonSuccessfulWithNotADate() throws Exception
    {
        AsDate named =
            new AsDate(
                () -> new Unnamed<>(Either.right(new Present<>("hello vasya"))),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a date of a certain format.", named.result().error());
    }

    @Test
    public void nonSuccessfulWithNotADateAndCustomErrorMessage() throws Exception
    {
        AsDate named =
            new AsDate(
                () -> new Unnamed<>(Either.right(new Present<>("hello vasya"))),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                "You must specify a valid date."
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a date of a certain format.", named.result().error());
    }

    @Test
    public void successfulWithAbsentField() throws Exception
    {
        AsDate named =
            new AsDate(
                () -> new Unnamed<>(Either.right(new Absent<>())),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithValidDate() throws Exception
    {
        AsDate named =
            new AsDate(
                () -> new Unnamed<>(Either.right(new Present<>("2019-05-25 08:07:54"))),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-05-25 08:07:54"), named.result().value().raw());
    }
}
