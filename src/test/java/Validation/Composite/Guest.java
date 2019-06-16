package Validation.Composite;

class Guest
{
    private String email;
    private String name;

    public Guest(String email, String name)
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
