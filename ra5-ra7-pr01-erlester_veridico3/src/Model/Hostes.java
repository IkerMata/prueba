package Model;

public class Hostes extends PersonaBona {
    private String dataNaix;
    private boolean alergies;

    public Hostes(String nom, String dni, String dataNaix, boolean alergies) {
        super(nom, dni);
        this.dataNaix = dataNaix;
        this.alergies = alergies;
    }

    @Override
    public String getNom() {
        return super.getNom();
    }

    @Override
    public String getDni() {
        return super.getDni();
    }


    public String getDataNaix() {
        return dataNaix;
    }
    public boolean getAlergies(){
        return alergies;
    }

}
