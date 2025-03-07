
public class Person {
    //Instance variables

    private String name;
    private String surname;
    private String email;

    // Constructor
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    //Creating getters for instance variables
    public String getName () {
        return name;
    }

    public String getSurname () {
        return surname;
    }

    public String getEmail () {
        return email;
    }

    // Creating setters for instance variables
    public void setName (String name){
        this.name = name;
    }

    public void setSurname (String surname){
        this.surname = surname;
    }

    public void setEmail (String email){
        this.email = email;
    }

    // Method to print personal information
    public void printPersonalInfo () {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Email: " + email);

    }
}





