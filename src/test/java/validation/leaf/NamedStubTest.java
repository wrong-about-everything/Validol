package validation.leaf;

import validation.leaf.is.NamedStub;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedStubTest
{
    @Test
    public void success() throws Exception
    {
        NamedStub<String> named = new NamedStub<>("vasya", Either.right(new Present<>("miliy")));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("miliy", named.result().value().raw());
    }

    @Test
    public void failure() throws Exception
    {
        NamedStub<String> named = new NamedStub<>("vasya", Either.left("ooops"));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("ooops", named.result().error());
    }
}
