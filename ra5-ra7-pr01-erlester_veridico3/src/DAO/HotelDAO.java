package DAO;
import Model.Hostes;
import Model.ReservaBona;
import Model.UsuariBo;

import java.util.List;

public interface HotelDAO {
    void guardarReserva(ReservaBona reserva, List<Hostes> hostes, UsuariBo usuari);

    void eliminarReserva(int numeroReservaEliminar);

    void modificarReserva(int numeroResrvaModificar, String novaDataEntrada, String novaDataSortida);
}