package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.format.email.IsEmail;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class IsEmailTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Exception
    {
        IsEmail named =
            new IsEmail(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    @UseDataProvider("invalidEmails")
    public void validationFailedWithInvalidEmail(String email) throws Exception
    {
        IsEmail named =
            new IsEmail(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(email))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid email.", named.result().error().value().get("message"));
    }

    @DataProvider
    public static Object[][] invalidEmails()
    {
        return
            new Object[][] {
                {"Woooops"},
                {"Woooops"},
                {"plainaddress"},
                {"#@%^%#$@#$@#.com"},
                {"@example.com"},
                {"Joe Smith <email@example.com>"},
                {"email.example.com"},
                {"email@example@example.com"},
                {".email@example.com"},
                {"email.@example.com"},
                {"email..email@example.com"},
                {"email@example.com (Joe Smith)"},
                {"email@example"},
                {"email@-example.com"},
                {"email@example.web"},
                {"email@111.222.333.44444"},
                {"email@example..com"},
                {"Abc..123@example.com"},
                {"”(),:;<>[\\]@example.com"},
                {"this\\ is\"really\"not\\allowed@example.com"},
                {"email@123.123.123.123"},
            };
    }

    @Test
    @UseDataProvider("validEmails")
    public void validationSucceededWithValidEmail(String email) throws Exception
    {
        IsEmail named =
            new IsEmail(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(email))
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(email, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] validEmails()
    {
        return
            new Object[][] {
                {"email@example.com"},
                {"firstname.lastname@example.com"},
                {"email@subdomain.example.com"},
                {"firstname+lastname@example.com"},
                {"email@[123.123.123.123]"},
                {"\"email\"@example.com"},
                {"1234567890@example.com"},
                {"email@example-one.com"},
                {"_______@example.com"},
                {"email@example.name"},
                {"email@example.museum"},
                {"email@example.co.jp"},
                {"firstname-lastname@example.com"},
            };
    }

    @Test
    public void validationSucceededWithEmptyEmail() throws Exception
    {
        IsEmail named =
            new IsEmail(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Absent<>())
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }
}
