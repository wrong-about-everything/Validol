package validation.Integration.NonCoincidingValidatableAndDataClass.ValidatableAndDataClassHaveDifferentStructure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ValidatableAndDataClassHaveDifferentStructureTest
{
    @Test
    public void requestWithInvalidDataClasses()
    {
        try {
            new ValidatedSampleRequest(
                new Gson().toJson(
                    Map.of(
                        "key1", 666,
                        "key2", "vasya"
                    ),
                    new TypeToken<Map<String, Object>>() {}.getType()
                )            )
                .result();
        } catch (Throwable e) {
            assertTrue(true);
            return;
        }

        fail("There is a programming error: no suitable constructor declared in SampleRequestData for ValidatedSampleRequest.");
    }
}
