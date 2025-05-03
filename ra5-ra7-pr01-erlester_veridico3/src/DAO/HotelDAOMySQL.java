package DAO;

import Model.Hostes;
import Model.ReservaBona;
import Model.UsuariBo;

import BBDDConnection.ConexioBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class HotelDAOMySQL implements HotelDAO {

    @Override
    public void guardarReserva(ReservaBona reserva, List<Hostes> hostes, UsuariBo usuari) {


        try {
            //INSERCCIO A LA TAULA RESERVABONA
            Connection con = ConexioBD.getInstance();
            String query = "INSERT INTO RESERVABONA (DATAENTRADA, DATASORTIDA, DNIUSUARI, " +
                    "ESMORZAR, NITS, PERSONES, PREU) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, reserva.getDataEntrada()); //dataentrad
            stmt.setString(2, reserva.getDataSortida()); //datasortida
            stmt.setString(3, reserva.getDniHosta(hostes.get(0))); //dniUsuari
            stmt.setBoolean(4, reserva.getEsmorzar()); //getEsmorzar
            stmt.setLong(5, reserva.getNits()); //nits
            stmt.setInt(6, reserva.getPersones()); //persones
            stmt.setDouble(7, reserva.getPreu()); //preu
            stmt.executeUpdate();
            //INSERCCIO A LA TAULA HOSTES
            for (int i = 0; i < reserva.getPersones(); i++) {
                String query2 = "INSERT INTO HOSTES (ALERGIES, DATANAX, DNI, " +
                        "DNIUSUARI, NOMBRE) " +
                        "VALUES ( ?, ?, ?, ?, ?)";
                PreparedStatement stmt2 = con.prepareStatement(query2);
                stmt2.setBoolean(1, reserva.getAlergiesHosta(hostes.get(i))); //alergies
                stmt2.setString(2, reserva.getdataNaixementHosta(hostes.get(i))); //dataNaix
                stmt2.setString(3, reserva.getDniHosta(hostes.get(i))); //dniHoste
                stmt2.setString(4, reserva.getDniHosta(hostes.get(0))); //dniUsuari
                stmt2.setString(5, reserva.getNomHosta(hostes.get(i))); //nomHoste
                stmt2.executeUpdate();
            }
            //INSERCCIO A LA TAULA USUARIBO
            String query3 = "INSERT INTO USUARIBO (NOM, DNI, CORREU, " +
                    "TELEFON) " +
                    "VALUES ( ?, ?, ?, ?)";
            PreparedStatement stmt3 = con.prepareStatement(query3);
            stmt3.setString(1, usuari.getNom()); //NOMUSUARI
            stmt3.setString(2, reserva.getDniHosta(hostes.get(0))); //DNIUSUARI
            stmt3.setString(3, usuari.getCorreu()); //CORREUUSUARI
            stmt3.setInt(4, usuari.getTelefon()); //TELEFONUSUARI
            stmt3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void eliminarReserva(int numeroReservaEliminar) {

        Connection con = ConexioBD.getInstance();

        HotelDAOCSV liniaEliminar = new HotelDAOCSV();

        String linia = liniaEliminar.llegirReserva(numeroReservaEliminar);
        String[] liniaSeparada = linia.split(",");
        String dniUsuari = liniaSeparada[1];

        try {

            String queryHostes = "DELETE FROM HOSTES WHERE DNIUSUARI = ?";
            String queryUsuari = "DELETE FROM USUARIBO WHERE DNI = ?";
            String queryReserva = "DELETE FROM RESERVABONA WHERE DNIUSUARI = ?";

            try (PreparedStatement stmt1 = con.prepareStatement(queryReserva);
                 PreparedStatement stmt2 = con.prepareStatement(queryHostes);
                 PreparedStatement stmt3 = con.prepareStatement(queryUsuari)) {

                stmt1.setString(1, dniUsuari);
                stmt2.setString(1, dniUsuari);
                stmt3.setString(1, dniUsuari);

                stmt3.executeUpdate();
                stmt2.executeUpdate();
                stmt1.executeUpdate();
            }

            System.out.println("------------------------------------------------------");
            System.out.println("La reserva ha estat eliminada correctament de la base de dades!");
            System.out.println("------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error en eliminar la reserva de la base de dades:");
            e.printStackTrace();
        }
    }


    @Override
    public void modificarReserva(int numeroResrvaModificar, String novaDataEntrada, String novaDataSortida) {

        Connection con = ConexioBD.getInstance();

        try {
            // FEM EL UPDATE DE LAS DATAS ENTRADA I SORTIDA EN LA BASE DE DA
            String updateQuery = "UPDATE RESERVABONA SET DATAENTRADA = ?, DATASORTIDA = ?";
            PreparedStatement stmt = con.prepareStatement(updateQuery);
            stmt.setString(1, novaDataEntrada);
            stmt.setString(2, novaDataSortida);

            int filesModificades = stmt.executeUpdate();

            if (filesModificades > 0) {
                System.out.println("------------------------------------------------------");
                System.out.println("Reserva modificada correctament en la base de dades.");
                System.out.println("------------------------------------------------------");
            } else {
                System.out.println("No s'ha trobat cap reserva amb aquest ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
