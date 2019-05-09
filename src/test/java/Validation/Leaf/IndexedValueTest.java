package Validation.Leaf;

import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IndexedValueTest
{
    @Test
    public void nonSuccessful() throws Exception
    {
        IndexedValue<?> named = new IndexedValue<>("vasya", new HashMap<String, String>());

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Key vasya does not exist", named.result().error());
    }

    @Test
    public void successful() throws Exception
    {
        IndexedValue<?> named = new IndexedValue<>("vasya", this.map("vasya", "belov"));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("belov", named.result().value());
    }

    private Map<String, String> map(String key, String value)
    {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);

        return map;
    }
}
