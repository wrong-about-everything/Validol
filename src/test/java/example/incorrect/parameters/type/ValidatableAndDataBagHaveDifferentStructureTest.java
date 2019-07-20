package example.incorrect.parameters.type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ValidatableAndDataBagHaveDifferentStructureTest
{
    @Test
    public void requestWithInvalidDataClasses()
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
            assertEquals(
                "You should create a constructor in class example.incorrect.parameters.type.SampleRequest with following arguments in exactly the same order: (com.google.gson.JsonPrimitive, java.lang.String)",
                e.getMessage()
            );
            return;
        }

        fail("There is a programming error: no suitable constructor declared in SampleRequest for ValidatedSampleRequest.");
    }
}
