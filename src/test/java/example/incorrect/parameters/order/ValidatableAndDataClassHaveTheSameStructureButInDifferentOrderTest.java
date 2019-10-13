package example.incorrect.parameters.order;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(DataProviderRunner.class)
public class ValidatableAndDataClassHaveTheSameStructureButInDifferentOrderTest
{
    @Test
    public void requestWithInvalidDataClasses() throws Throwable
    {
        try {
            new ValidatedSampleRequest(
                new Gson().toJson(
                    Map.of(
                        "key1", 777,
                        "key2", "vasya"
                    ),
                    new TypeToken<Map<String, Object>>() {}.getType()
                )            )
                .result();
        } catch (Throwable e) {
            assertTrue(true);
            return;
        }

        fail("There is a programming error: no suitable constructor declared in SampleRequest for ValidatedSampleRequest.");
    }
}
