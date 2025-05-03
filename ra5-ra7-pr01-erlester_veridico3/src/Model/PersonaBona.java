
package Model;

public class PersonaBona {
    private String nom;
    private String dni;

    public PersonaBona(String nom, String dni){
        this.nom = nom;
        this.dni = dni;
    }
    public String getDni() {
        return dni;
    }

    public String getNom() {
        return nom;
    }
}
