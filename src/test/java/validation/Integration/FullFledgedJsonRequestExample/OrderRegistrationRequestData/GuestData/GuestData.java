package validation.Integration.FullFledgedJsonRequestExample.OrderRegistrationRequestData.GuestData;

public class GuestData
{
    private String email;
    private String name;

    public GuestData(String email, String name)
    {
        this.email = email;
        this.name = name;
    }

    public String email()
    {
        return this.email;
    }

    public String name()
    {
        return this.name;
    }
}
