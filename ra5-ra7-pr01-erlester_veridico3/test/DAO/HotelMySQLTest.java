package DAO;

import Model.Hostes;
import Model.ReservaBona;
import Model.UsuariBo;
import DAO.HotelDAOMySQL;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HotelMySQLTest {

    @org.junit.jupiter.api.Test
    void guardarReservaTest() {
        UsuariBo usr = new UsuariBo("Biel","45645645B","caca@gmail.com",456456456);
        ReservaBona rb = new ReservaBona(2,"2025-05-05","2025-05-08",true);
        ArrayList<Hostes> hostes = new ArrayList<>();
        for(int i  = 0 ; i < rb.getPersones(); i++){
            if(i == 0){
                hostes.add(new Hostes(usr.getNom(),usr.getDni(),"2006-05-02",false));
            }else{
                hostes.add(new Hostes("Juan","12421524N","2001-02-05", true));
            }
        }
        DAO.HotelDAOMySQL dao = new HotelDAOMySQL();
        dao.guardarReserva(rb,hostes,usr);
    }
}