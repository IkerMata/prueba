package DAO;
import Model.*;
import com.sun.tools.javac.Main;

import javax.naming.spi.ResolveResult;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.net.ProxySelector;
import java.util.*;

public class HotelDAOCSV implements HotelDAO{

    private static Path path = Path.of("files","reserves.csv");
    private static File file = new File(path.toString());

    @Override
    public void guardarReserva(ReservaBona reserva, List<Hostes> hostes, UsuariBo usuari) {
        try {
            FileWriter myWriter = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write(reservaToCSV(reserva, hostes, usuari));
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("S'ha produit un error a l'hora de escriure al fitxer .csv");
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarReserva(int numeroReservaEliminar) {
        List<String> reservesRestants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linia;
            int indexActual = 0;
            while ((linia = br.readLine()) != null) {
                if (indexActual != numeroReservaEliminar) {
                    reservesRestants.add(linia);
                }
                indexActual++;
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : reservesRestants) {
                            bw.write(l);
                    bw.newLine();
                }
                System.out.println("------------------------------------------------------");
                System.out.println("La reserva ha estat eliminada correctament del fitxer!");
                System.out.println("------------------------------------------------------");
            } catch (IOException e) {
                System.out.println("ERROR! al escriure al fitxer després d'eliminar reserva.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("ERROR! al llegir el fitxer per eliminar reserva.");
            e.printStackTrace();
        }
    }

    @Override
    public void modificarReserva(int numeroResrvaModificar, String novaDataEntrada, String novaDataSortida) {
        HotelDAOCSV liniaModificar = new HotelDAOCSV();
        String linia = liniaModificar.llegirReserva(numeroResrvaModificar);
        List<String> lineasActualizadas = new ArrayList<>();

        if (linia == null) {
            System.out.println("Reserva no trobada.");
            return;
        }

        String[] liniaSeparada = linia.split(",");
        String dataEntrada = liniaSeparada[5];
        String dataSortida = liniaSeparada[6];



        // Actualitzar les dates a la línia
        liniaSeparada[5] = novaDataEntrada;
        liniaSeparada[6] = novaDataSortida;
        String novaLinia = String.join(",", liniaSeparada);

        // Llegir tot el fitxer i substituir la línia corresponent
        try (BufferedReader br = new BufferedReader(new FileReader("files/reserves.csv"))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.equals(linia)) {
                    lineasActualizadas.add(novaLinia);
                } else {
                    lineasActualizadas.add(l);
                }
            }
        } catch (IOException e) {
            System.out.println("Error llegint el fitxer: " + e.getMessage());
            return;
        }

        // Escriure les línies actualitzades
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("files/reserves.csv"))) {
            for (String l : lineasActualizadas) {
                bw.write(l);
                bw.newLine();
            }
            System.out.println("------------------------------------------------------");
            System.out.println("Reserva modificada correctament en el document CSV.");
            System.out.println("------------------------------------------------------");
        } catch (IOException e) {
            System.out.println("Error escrivint el fitxer: " + e.getMessage());
        }
    }


    public List<String> agafarLlistaDnis() {
        ArrayList<String> llistaDnis = new ArrayList<>();
        try {
            File f = new File(file.toString());
            Scanner lectorFitxer = new Scanner(f);
            while (lectorFitxer.hasNextLine()) {
                String linia = lectorFitxer.nextLine();
                String[] liniaSeparada = linia.split(",");
                int nombrePersones = Integer.parseInt(liniaSeparada[4]);
                String[] hostesSeparats = liniaSeparada[8].split(";");
                for (int i = 0; i < nombrePersones; i++) {
                    String[] atributsHostes = hostesSeparats[i].split(":");
                    String dniHostes = atributsHostes[1];
                    llistaDnis.add(dniHostes);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR! Ha ocorregut un error a l'hora de llegir el fitxer csv.");
            e.printStackTrace();
        }
        return llistaDnis;
    }
    public String llegirReserva(int idReserva) {
        String linia = "";
        try {
            File f = new File(file.toString());
            Scanner introduit = new Scanner(f);

            // Saltar les línies fins arribar a la indicada pel 'numero'
            int liniaBuscant = 0;
            while (liniaBuscant != idReserva) {
                introduit.nextLine();
                liniaBuscant++;
            }

            // Quan arribem a la línia indicada, llegim les dades i les convertim en un objecte Wok
            if (introduit.hasNextLine()) {
                String data = introduit.nextLine();
                linia = data;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR! No s'ha pogut trobar la teva reserva");
            e.printStackTrace();
        }
        return linia;
    }

    public void imprimirReservesResum() {
        try {
            File f = new File(file.toString());
            Scanner scan = new Scanner(f);
            int numeroReserva = 1;

            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                String[] parts = data.split(",");

                String nom = parts[0]; // Nom
                String dni = parts[1]; //dni
                String dataEntrada = parts[5]; // dataentrada
                String dataSortida = parts[6]; //datasortida
                int nombrePersones = Integer.parseInt(parts[4]); //Nombre de persones

                System.out.println(numeroReserva + ") Reserva numero: " + numeroReserva );
                System.out.println("- Nom Usuari: " + nom);
                System.out.println("- Dni Usuri: " + dni);
                System.out.println("- Data de Entrada: " + dataEntrada);
                System.out.println("- Data de Sortida: " + dataSortida);
                System.out.println("- Nombre de persones: " + nombrePersones);

                // Incrementar el número de reserva per la següent línia
                numeroReserva++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR! No hem pogut mostrar el resum de les reserves");
            e.printStackTrace();
        }
    }

    private String reservaToCSV(ReservaBona reserva, List<Hostes> hostes, UsuariBo usuari) {
        StringBuilder csv = new StringBuilder();
        //Creem Usuari
        csv.append(reserva.getNomHosta(hostes.get(0))).append(","); //nomUsuari
        csv.append(reserva.getDniHosta(hostes.get(0))).append(","); //dniUsuari
        csv.append(reserva.getCorreuUsuari(usuari)).append(","); //correu Usuari
        csv.append(reserva.getTelefonUsuari(usuari)).append(","); //telefon Usuari
        //Creem la reserva
        csv.append(reserva.getPersones()).append(","); // nombre de persones
        csv.append(reserva.getDataEntrada()).append(","); //data entrada
        csv.append(reserva.getDataSortida()).append(","); //data sortida
        csv.append(reserva.getEsmorzar()).append(","); //esmorzar inclos o no
        //Creem els hostes
        for(int i = 0; i < reserva.getPersones(); i++){ //escribim caracteristiqes de cada hoste
            csv.append(reserva.getNomHosta(hostes.get(i))).append(":");
            csv.append(reserva.getDniHosta(hostes.get(i))).append(":");
            csv.append(reserva.getAlergiesHosta(hostes.get(i))).append(":");
            csv.append(reserva.getdataNaixementHosta(hostes.get(i))).append(";");
        }
        csv.append(",");
        return csv.toString();
    }
    public int contadorLinies() {
        int linies = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.readLine() != null) {
                linies++;
            }
        } catch (IOException e) {
            System.out.println("ERROR! S'ha produit un error a l'hora de llegir el fitxer .csv");
            e.printStackTrace();
        }
        return linies;
    }
}
