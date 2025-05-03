
package Model;

public class UsuariBo extends PersonaBona{
    private String correu;
    private int telefon;

    public UsuariBo(String nom, String dni, String correu, int telefon) {
        super(nom, dni);
        this.correu = correu;
        this.telefon = telefon;
    }

    public String getCorreu() {
        return correu;
    }
    public int getTelefon() {
        return telefon;
    }
}
