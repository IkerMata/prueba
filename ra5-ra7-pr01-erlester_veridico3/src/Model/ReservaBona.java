package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservaBona {
    private int persones;
    private String dataEntrada;
    private String dataSortida;
    private long nits;
    private boolean esmorzar;
    private double preu;
    private List<Hostes> hostes;

    //CREEM UNA RESERVA
    public ReservaBona (int persones, String dataEntrada, String dataSortida, boolean esmorzar){
        this.persones = persones;
        this.dataEntrada = dataEntrada;
        this.dataSortida = dataSortida;
        this.esmorzar = esmorzar;
        this.hostes =  new ArrayList<>();
        setNits();
        calcularPreu();
    }
    public String getNomHosta(Hostes hostes){
        return hostes.getNom();
    }
    public String getDniHosta(Hostes hostes){
        return hostes.getDni();
    }
    public boolean getAlergiesHosta(Hostes hostes){
        return hostes.getAlergies();
    }
    public String getdataNaixementHosta(Hostes hostes){
        return hostes.getDataNaix();
    }
    //metodes per el Usuari
    public String getCorreuUsuari(UsuariBo usuari){return usuari.getCorreu();}
    public int getTelefonUsuari(UsuariBo usuari){return usuari.getTelefon();}
    //Per obtenir el dni les alergies i la data de naixement cal cridar als metodes de hostes pero fent el get(1)


    public void setHostes(List<Hostes> hostes) {
        this.hostes = hostes;
    }

    //CODIS PER PERSONES
    public int getPersones(){
        return persones;
    }
    //DATES DE ENTRADA
    public String getDataEntrada() {
        return dataEntrada;
    }
    //DATES DE SORTIDA
    public String getDataSortida() {
        return dataSortida;
    }
    //NITS
    public long getNits() {
        return nits;
    }
    private void setNits(){
        // Definim el format de la data en DD/MM/YYYY
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convertim les dates d'entrada i sortida a objectes LocalDate
        LocalDate entrada = LocalDate.parse(this.dataEntrada, formatter);
        LocalDate sortida = LocalDate.parse(this.dataSortida, formatter);

        // Calculam la diferència en dies (nits)
        long nits = java.time.temporal.ChronoUnit.DAYS.between(entrada, sortida);

        // Assignem el resultat a la variable nits
        this.nits = nits;
    }
    //ESMORZAR
    public boolean getEsmorzar(){
        return esmorzar;
    }
    //PREU
    public double getPreu() {
        return preu;
    }
    private void calcularPreu() {;
        double preuNit = 72.3;
        double preuEsmorzar = 8.00;
        //calculem el preu de nit
        preuNit = preuNit * this.persones * this.nits;
        //creem la variable preu total esmorzar per calcular el preu total de quan costa el esmorzar segons els usuaris i nits

        double preuTotalEsmorzar = 0;
        if (this.esmorzar) {
            preuTotalEsmorzar = preuEsmorzar * persones * nits;
        }

        this.preu = preuNit + preuTotalEsmorzar;
    }


}
