import Model.Hostes;
import Model.PersonaBona;
import Model.ReservaBona;
import Model.UsuariBo;
import Utils.UtilsHotel;
import DAO.*;
import DAO.HotelDAOMySQL;
import DAO.HotelDAOCSV;
import jdk.swing.interop.SwingInterOpUtils;

import java.rmi.server.RemoteRef;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainHotelBona {
    public static void main(String[] args) {
        while(true) {
            int instruccions = menu();
            if (instruccions == 1) {
                System.out.println("------------------------------------------------------");
                System.out.println("Has escollit l'opció de fer una reserva");
                System.out.println("------------------------------------------------------");
                UsuariBo usuari = crearUsuari();
                ReservaBona reserva = crearReserva();
                List<Hostes> hostes = crearHostes(reserva.getPersones(), usuari, reserva);
                reserva.setHostes(hostes);
                HotelDAOCSV dao = new HotelDAOCSV();
                dao.guardarReserva(reserva, hostes, usuari);
                DAO.HotelDAOMySQL dao2 = new HotelDAOMySQL();
                dao2.guardarReserva(reserva, hostes, usuari);
                imprimirReserva(reserva, usuari, hostes);
            } else if (instruccions == 2) {
                System.out.println("------------------------------------------------------");
                System.out.println("Has escollit l'opció d'eliminar la reserva");
                System.out.println("------------------------------------------------------");
                eliminarReservaEScollida();
            } else if (instruccions == 3) {
                System.out.println("------------------------------------------------------");
                System.out.println("Has escollit l'opció de modificar la reserva");
                System.out.println("------------------------------------------------------");

                modificarReservaEscollida();

            } else if (instruccions == 4) {
                System.out.println("------------------------------------------------------");
                System.out.println("Has escollit l'opció de llegir la reserva");
                System.out.println("------------------------------------------------------");

                imprimirReservaEscollida();

            } else if (instruccions == 5) {
                System.out.println("------------------------------------------------------");
                System.out.println("GRACIES PER UTILITZAR LA NOSTRE APP, FINS LA PROXIMA :)");
                System.out.println("------------------------------------------------------");
                break;
            }
            //TODO:  PREGUNTAR SI VOL SEGUIR NAVEGANT A LA APP O SI VOL SORTIR D'AQUESTA
        }
    }
    public static void modificarReservaEscollida(){
        Scanner introduit = new Scanner(System.in);
        HotelDAOCSV hotelCSV = new HotelDAOCSV();
        HotelDAOMySQL hotelDAOMySQL = new HotelDAOMySQL();
        hotelCSV.imprimirReservesResum();
        System.out.print("Reserva a actualitzar la data: ");
        int reservaEscollida = comprovarLiniesCSV();
        String validar = "^\\d{2}-\\d{2}-\\d{4}$";
        int[] diesPerMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String dataEntradaFinal = "", dataSortidaFinal = "";
        int dies, mesos, anys;

        System.out.print("Introdueix la nova data d'entrada a l'hotel (DD-MM-YYYY): ");
        while (true) {
            String dataEntrada = introduit.nextLine();
            if (dataEntrada.matches(validar)) {
                String[] llistaEntrada = dataEntrada.split("-");
                dies = Integer.parseInt(llistaEntrada[0]);
                mesos = Integer.parseInt(llistaEntrada[1]);
                anys = Integer.parseInt(llistaEntrada[2]);

                if (anys < 2025) {
                    System.out.print("ERROR: Any incorrecte, torna-ho a provar: ");
                } else if (mesos < 1 || mesos > 12) {
                    System.out.print("ERROR: Mes incorrecte, torna-ho a provar: ");
                } else if (dies < 1 || dies > diesPerMes[mesos - 1]) {
                    System.out.print("ERROR: Dia incorrecte, torna-ho a provar: ");
                } else {
                    dataEntradaFinal = dataEntrada;
                    break;
                }
            } else {
                System.out.print("ERROR: Format incorrecte. Introdueix la data (DD-MM-YYYY): ");
            }
        }

        System.out.print("Introdueix la nova data de sortida de l'hotel (DD-MM-YYYY): ");
        while (true) {
           String  dataSortida = introduit.nextLine();
            if (dataSortida.matches(validar)) {
                String[] llistaSortida = dataSortida.split("-");
                dies = Integer.parseInt(llistaSortida[0]);
                mesos = Integer.parseInt(llistaSortida[1]);
                anys = Integer.parseInt(llistaSortida[2]);

                String[] llistaEntrada = dataEntradaFinal.split("-");
                int diaEntrada = Integer.parseInt(llistaEntrada[0]);
                int mesEntrada = Integer.parseInt(llistaEntrada[1]);
                int anyEntrada = Integer.parseInt(llistaEntrada[2]);

                if (anys < 2025) {
                    System.out.print("ERROR: Any incorrecte, torna-ho a provar: ");
                } else if (mesos < 1 || mesos > 12) {
                    System.out.print("ERROR: Mes incorrecte, torna-ho a provar: ");
                } else if (dies < 1 || dies > diesPerMes[mesos - 1]) {
                    System.out.print("ERROR: Dia incorrecte, torna-ho a provar: ");
                } else if (anys < anyEntrada || (anys == anyEntrada && mesos < mesEntrada) || (anys == anyEntrada && mesos == mesEntrada && dies <= diaEntrada)) {
                    System.out.print("ERROR: La data de sortida ha de ser posterior a la d'entrada. Torna-ho a provar: ");
                } else {
                    dataSortidaFinal = dataSortida;
                    break;
                }
            } else {
                System.out.print("ERROR: Format incorrecte. Introdueix la data (DD-MM-YYYY): ");
            }
        }

        // Convertir a format YYYY-MM-DD per guardar al CSV
        String[] dataEntradaOrdenadaLlista = dataEntradaFinal.split("-");
        String dataEntradaOrdenada = dataEntradaOrdenadaLlista[2] + "-" + dataEntradaOrdenadaLlista[1] + "-" + dataEntradaOrdenadaLlista[0];

        String[] dataSortidaOrdenadaLlista = dataSortidaFinal.split("-");
        String dataSortidaOrdenada = dataSortidaOrdenadaLlista[2] + "-" + dataSortidaOrdenadaLlista[1] + "-" + dataSortidaOrdenadaLlista[0];

        hotelDAOMySQL.modificarReserva(reservaEscollida,dataEntradaOrdenada,dataSortidaOrdenada);
        hotelCSV.modificarReserva(reservaEscollida,dataEntradaOrdenada,dataSortidaOrdenada);
    }

    public static void eliminarReservaEScollida(){
        HotelDAOCSV hotelCSV = new HotelDAOCSV();
        HotelDAOMySQL hotelDAOMySQL = new HotelDAOMySQL();
        hotelCSV.imprimirReservesResum();
        System.out.print("Reserva a eliminar: ");
        int reservaEscollida = comprovarLiniesCSV();
        //eliminem la reserva escollida
        hotelDAOMySQL.eliminarReserva(reservaEscollida);
        hotelCSV.eliminarReserva(reservaEscollida);
    }

    //funcio que comprova que el usuari fiqui un bon input alhora d'escollir quina linia del csv vol mostrar, modificar o eliminar
    public static int comprovarLiniesCSV(){
        Scanner introduit = new Scanner(System.in);
        HotelDAOCSV hotel = new HotelDAOCSV();
        //aqui el usuari escolleix quina reserva vol escollir
        int idReservaNum = 0;
        while(true){
            String idReserva = introduit.nextLine();
            try{
                idReservaNum = Integer.parseInt(idReserva);
            }catch (NumberFormatException e){
                System.out.print("ERROR! Cal introduir un numero: ");
                continue;
            }
            if (idReservaNum <= 0 || idReservaNum >= hotel.contadorLinies()) {
                System.out.print("ERROR! Cal introduir un valor valid: ");
            }else{
                break;
            }
        }
        idReservaNum -= 1;
        return idReservaNum;
    }
    public static void imprimirReservaEscollida() {
        HotelDAOCSV imprimirReservaCSV = new HotelDAOCSV();
        Scanner introduit = new Scanner(System.in);
        System.out.println("=== Escolleix de quina reserva vols veure el menú ===");
        imprimirReservaCSV.imprimirReservesResum(); //imprimeix resum de totes les reserves
        System.out.print("Reserva a mostrar: ");
        int idReservaNum = comprovarLiniesCSV();

        //Ja hem verificat que estigui ven ficat el idReserva, ara nomes falta imprimir correctament el resum de la reserva
        String linia = imprimirReservaCSV.llegirReserva(idReservaNum);
        String [] liniaSeparada = linia.split(",");
        //CREEM L'USUARI
        String nomUsuari = liniaSeparada[0];
        String dniUsuari = liniaSeparada[1];
        String emailUsuari = liniaSeparada[2];
        int telefonUsuari = Integer.parseInt(liniaSeparada[3]);
        UsuariBo usuariImprimir = new UsuariBo(nomUsuari,dniUsuari,emailUsuari,telefonUsuari); //creem el usuari
        //CREEM LA RESERVA
        int personesReserva = Integer.parseInt(liniaSeparada[4]);
        String dataEntrada = liniaSeparada[5];
        String dataSortida = liniaSeparada[6];
        boolean esmorzarReserva = Boolean.getBoolean(liniaSeparada[7]);
        ReservaBona reservaImprimir = new ReservaBona(personesReserva,dataEntrada,dataSortida,esmorzarReserva);
        //CREEM ELS HOSTES
        List<Hostes> hostesImprimir = new ArrayList<>();
        String [] hostesSeparats = liniaSeparada[8].split(";");
        for(int i = 0; i < reservaImprimir.getPersones(); i++){ //agafem les caracteristiques de cada hoste i les guardem dins d'una llista d'hostes
            String [] atributsHostes = hostesSeparats[i].split(":");
            String nomHostes = atributsHostes[0];
            String dniHostes = atributsHostes[1];
            String dataNaixHostes = atributsHostes[2];
            boolean alergiesHostes = Boolean.getBoolean(atributsHostes[3]);
            hostesImprimir.add(new Hostes(nomHostes,dniHostes,dataNaixHostes,alergiesHostes)) ;
        }
        imprimirReserva(reservaImprimir,usuariImprimir,hostesImprimir);
    }


    //CREEM LA FUNCIO DEL MENÚ
    public static int menu(){
        Scanner introduit = new Scanner(System.in);
        System.out.println("-- MENÚ APLICACIÓ D'HOTEL IKER I BIEL --");
        int escollit = 0;
        while(true){
            System.out.println("1. Afegir reserva");
            System.out.println("2. Eliminar reserva (has de tenir una reserva feta previament)");
            System.out.println("3. Modificar reserva (has de tenir una reserva feta previament)");
            System.out.println("4. Llegir reserva (has de tenir una reserva feta previament)");
            System.out.println("5. Sortir de la APP");
            System.out.println("------------------------------------------------------");
            System.out.print("Escolleix quina opcio vols realitzar: ");
            String opcio = introduit.nextLine();
            System.out.println("------------------------------------------------------");
            try{
                int opcioInt = Integer.parseInt(opcio);
                if(opcioInt >= 1 && opcioInt <=5){
                    escollit = opcioInt;
                    break;
                }else{
                    System.out.println();
                    System.out.println("---- Introdueix un valor valid (entre el 1 i el 5)");
                }
            } catch(NumberFormatException e){
                System.out.println("---- Introdueix un NUMERO i ha de sr entre el 1 i 5!!");
            }
        }
        return escollit;
    }
    //CREEM EL VERIFICADOR DE NOM
    public static String verificarNom(){
        Scanner introduit = new Scanner(System.in);
        System.out.print("Introdueix el nom: ");
        while(true){
            //creem la variable de caracters per tal de verificar que tot el nom estigui fet per lletres
            int caracters = 0;
            String nom = introduit.nextLine();
            char [] llistaNom = nom.toCharArray();
            for(int i = 0; i < llistaNom.length; i++){
                //Si ens trobem amb una lletra se li suma un a la variable caracters
                if(Character.isLetter(llistaNom[i])){
                    caracters++;
                }else{
                    break;
                }
            }
            //si tots els caracters del nom son lletres sortim del bucle  guardem la variable nom si no tornem a demanar que introdueixi be el nom
            if(caracters == llistaNom.length){
                return nom;
            }else{
                System.out.print("ERROR! Introdueix correctament el Nom: ");
            }
        }
    }
    //CREEM EL VERIFICADOR DE DNI
    public static String verificarDni(){
        Scanner introduit = new Scanner(System.in);
        HotelDAOCSV verificarDni = new HotelDAOCSV();
        //FEM QUE EL USUARI INTRODUEIXI CORRECTAMENT EL DNI
        System.out.print("Introdueix el Dni: ");
        while (true) {
            boolean repetit = false;
            boolean comprovacio = false; //si pasa la verificacio i es fa la comprovacio passa a ser true
            String dni = introduit.nextLine();
            char[] llistaDni = dni.toCharArray();
            //declarem les variables
            int contarNombres = 0;
            boolean lletraUltima = false;

            try {
                if (Character.isAlphabetic(llistaDni[llistaDni.length - 1]) && Character.isUpperCase(llistaDni[llistaDni.length - 1])) {
                    lletraUltima = true;
                }
                for (int i = 0; i < llistaDni.length - 1; i++) {
                    if (Character.isDigit(llistaDni[i])) {
                        contarNombres++;
                    }
                }
                if (lletraUltima && contarNombres == 8) {
                    while (true){
                        ArrayList <String> llistaDnis = new ArrayList<>(verificarDni.agafarLlistaDnis());
                        for(int i = 0; i < llistaDnis.size()-1; i++){
                            if(!(dni.equals(llistaDnis.get(i)))){
                                continue;
                            }else{
                                repetit = true;
                                comprovacio = true;
                                break;
                            }
                        }
                        if(repetit){
                            System.out.print("ERROR! El Dni introduit ja ha estat inserit a la Base de dades, escriu-lo correctament: ");
                            break;
                        }else{
                            return dni;
                        }
                    }

                } else {
                    if((repetit = false) && (comprovacio == false)){
                        continue;
                    }else{
                        System.out.print("ERROR! Introdueix correctament el Dni: ");
                    }

                }
            } catch (Exception e) {
                System.out.print("ERROR! Introdueix correctament el Dni:");
            }
        }
    }

    //CREEM EL CONSTRUCTOR D'USUARI
    public static UsuariBo crearUsuari(){
        System.out.println("=== Introdueix les dades de l'usuari que fa la reserva ===");
        Scanner introduit = new Scanner(System.in);
        HotelDAOCSV verificarDni = new HotelDAOCSV();

        //CREEM LES DUES VARIABLES
        String nomFinal = "";
        String dniFinal = "";
        String correuFinal = "";
        int telefonFinal = 0;

        //PRIMER VERIFIQUEM EL NOM I EL DNI
        nomFinal = verificarNom();
        dniFinal = verificarDni();
        //VERIFIQUEM EL CORREU
        boolean arroba = false, punto = false;
        while (true) {
            System.out.print("Introdueix el teu correu electrònic: ");
            String correu = introduit.nextLine();
            for (char p : correu.toCharArray()) {
                if (p == '@') arroba = true;
                if (p == '.') punto = true;
            }

            if (arroba && punto) {
                int posPunto = correu.lastIndexOf('.');
                if (posPunto != -1 && correu.length() - posPunto - 1 == 3) {
                    String dominio = correu.substring(posPunto + 1);
                    if (dominio.matches("[a-zA-Z]{3}")) {
                        correuFinal = correu;
                        break;
                    } else {
                        System.out.println("ERROR: El domini ha de contenir només 3 lletres després del punt.");
                    }
                } else {
                    System.out.println("ERROR: El domini ha de contenir exactament 3 caràcters després del punt.");
                }
            } else {
                System.out.println("ERROR: El correu electrònic ha de contenir un '@' i un '.'");
            }
            arroba = false;
            punto = false;
        }

        //ARA VERIFIQUEM EL TELEFON
        System.out.print("Introdueix el teu telèfon: ");
        while (true) {
            int contadorNombres = 0;
            String numero = introduit.nextLine();
            char[] numeroSeparat = numero.toCharArray();
            for(int i = 0; i < numeroSeparat.length; i++){
                if(Character.isDigit(numeroSeparat[i])){
                    contadorNombres++;
                }
            }
            if(contadorNombres == 9){
                int numeroCambiat = Integer.parseInt(numero);
                telefonFinal = numeroCambiat;
                break;
            }else{
                System.out.print("ERROR! Número de telèfon incorrecte, torna a provar-ho: ");
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------");

        return new UsuariBo(nomFinal,dniFinal,correuFinal,telefonFinal);
    }

    //CREEM EL CONSTRUCTOR DE RESERVES
    public static ReservaBona crearReserva(){
        Scanner introduit = new Scanner(System.in);

        System.out.println();
        System.out.println("=== Introdueix les dades de la Reserva ===");
        //CREEM LES VARIABLES
        int personesFinals = 0;
        String dataEntradaFinal = "";
        String dataSortidaFinal = "";
        boolean esmorzarFinal = false;
        List<Hostes> hostesLlista;

        //VERIFIQUEM QUE SIGUI CORRECTE LES PERSONES
        while(true) {
            int persones = UtilsHotel.llegirInt(introduit, "Introdueix el número de persones (entre 1 i 5): ");
            if(persones > 0 && persones <= 5) {
                personesFinals = persones;
                break;
            }else{
                System.out.println(" !!INTRODUEIX UN VALOR VALID!!");
            }
        }

        //VERIFIQUEM LES NITS FINALS (PRIMER LA DE ENTRADA I DESPRES LA DE SORTIDA)
        String validar = "^\\d{2}-\\d{2}-\\d{4}$";
        int [] diesPerMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        System.out.print("Introdueix la data que vols entrar a l'hotel (DD-MM-YYYY): ");
        int dies, mesos, anys;

        while(true){
            String dataEntrada = introduit.nextLine();
            if(dataEntrada.matches(validar)){
                String [] llistaEntrada = dataEntrada.split("-");
                dies = Integer.parseInt(llistaEntrada[0]);
                mesos = Integer.parseInt(llistaEntrada[1]);
                anys = Integer.parseInt(llistaEntrada[2]);

                if(anys < 2025){
                    System.out.print("ERROR: Any incorrecte, torna-ho a provar: ");
                }else if (mesos < 1 || mesos > 12){
                    System.out.print("ERROR: Mes incorrecte, torna-ho a provar: ");
                }else if(dies < 1 || dies > diesPerMes[mesos - 1]){
                    System.out.print("ERROR: Dia incorrecte, torna-ho a provar: ");
                }else{
                    dataEntradaFinal = dataEntrada;
                    break;
                }
            }else{
                System.out.print("ERROR: Format incorrecte. Introdueix la data (DD-MM-YYYY): ");
            }
        }

        System.out.print("Introdueix la data que vols sortir de l'hotel (DD-MM-YYYY): ");

        while(true) {
            String dataSortida = introduit.nextLine();

            if (dataSortida.matches(validar)) {
                String[] llistaSortida = dataSortida.split("-");
                dies = Integer.parseInt(llistaSortida[0]);
                mesos = Integer.parseInt(llistaSortida[1]);
                anys = Integer.parseInt(llistaSortida[2]);

                String[] llistaEntrada = dataEntradaFinal.split("-");
                int diaEntrada = Integer.parseInt(llistaEntrada[0]);
                int mesEntrada = Integer.parseInt(llistaEntrada[1]);
                int anyEntrada = Integer.parseInt(llistaEntrada[2]);

                if (anys < 2025) {
                    System.out.print("ERROR: Any incorrecte, torna-ho a provar: ");
                } else if (mesos < 1 || mesos > 12) {
                    System.out.print("ERROR: Mes incorrecte, torna-ho a provar: ");
                } else if (dies < 1 || dies > diesPerMes[mesos - 1]) {
                    System.out.print("ERROR: Dia incorrecte, torna-ho a provar: ");
                }else if (anys < anyEntrada || (anys == anyEntrada && mesos < mesEntrada) || (anys == anyEntrada && mesos == mesEntrada && dies <= diaEntrada)) {
                    System.out.print("ERROR: La data de sortida ha de ser posterior a la d'entrada. Torna-ho a provar: ");
                } else {
                    dataSortidaFinal = dataSortida;
                    break;
                }
            } else {
                System.out.print("ERROR: Format incorrecte. Introdueix la data (DD-MM-YYYY): ");
            }
        }
        //Cambiem el ordre de la dataEntrada
        String [] dataEntradaOrdenadaLlista = dataEntradaFinal.split("-");
        String dataEntradaOrdenada = dataEntradaOrdenadaLlista[2] + "-" + dataEntradaOrdenadaLlista[1] + "-" + dataEntradaOrdenadaLlista[0];
        //Cambiem el ordre de la dataSortida
        String [] dataSortidaOrdenadaLlista = dataSortidaFinal.split("-");
        String dataSortidaOrdenada = dataSortidaOrdenadaLlista[2] + "-" + dataSortidaOrdenadaLlista[1] + "-" + dataSortidaOrdenadaLlista[0];

        System.out.println("-- Vols afegir esmorzar? --");

        System.out.print("Introdueix 'si' o 'no': ");
        while(true) {
            String esmorzar = introduit.nextLine();
            if (esmorzar.toUpperCase().equals("SI")) {
                esmorzarFinal = true;
                break;
            } else if (esmorzar.toUpperCase().equals("NO")) {
                esmorzarFinal = false;
                break;
            } else {
                System.out.print("ERROR: Resposta incorrecta. Escriu 'si' o 'no': ");
            }
        }
        System.out.println();
        System.out.println("------------------------------------------------------");

        return new ReservaBona(personesFinals, dataEntradaOrdenada, dataSortidaOrdenada, esmorzarFinal);
    }

    //CREEM EL CONSTRUCTOR D'HOSTES
    public static List<Hostes> crearHostes(int nombrePersones, UsuariBo usuari, ReservaBona reserva) {
        List<Hostes> hostes = new ArrayList<>();
        Scanner introduit = new Scanner(System.in);

        System.out.println();
        System.out.println("=== Introdueix les dades dels hostes ===");

        for (int i = 0; i < nombrePersones; i++) {
            String nomFinal = "";
            String dniFinal = "";

            if (i == 0) {
                System.out.println("=== Les dades del hoste 1 són les del Usuari ===");
                System.out.println("El nom del hoste és: " + usuari.getNom());
                nomFinal = usuari.getNom();
                System.out.println("El dni del hoste és: " + usuari.getDni());
                dniFinal = usuari.getDni();
            } else {
                System.out.println("=== Introdueix les dades per al hoste " + (i + 1) + " ===");
                nomFinal = verificarNom();
                dniFinal = verificarDni();
            }

            String dataNaixFinal = "";
            boolean alergiesFinals = false;
            String validar = "^\\d{2}-\\d{2}-\\d{4}$";
            int[] diesPerMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            while (true) {
                System.out.print("Data de naixement (DD-MM-YYYY): ");
                String dataNaix = introduit.nextLine();

                if (dataNaix.matches(validar)) {
                    String[] llistaEntrada = dataNaix.split("-");
                    int dies = Integer.parseInt(llistaEntrada[0]);
                    int mesos = Integer.parseInt(llistaEntrada[1]);
                    int anys = Integer.parseInt(llistaEntrada[2]);

                    if (mesos < 1 || mesos > 12) {
                        System.out.print("ERROR: Mes incorrecte, torna-ho a provar: ");
                    } else if (dies < 1 || dies > diesPerMes[mesos - 1]) {
                        System.out.print("ERROR: Dia incorrecte, torna-ho a provar: ");
                    } else {
                        dataNaixFinal = dataNaix;
                        break;
                    }
                } else {
                    System.out.print("ERROR! Introdueix la data en el format DD-MM-YYYY: ");
                }
            }
            //Cambiem el ordre de la dataSortida
            String [] dataNaixLlista = dataNaixFinal.split("-");
            String dataNaixOrdenada = dataNaixLlista[2] + "-" + dataNaixLlista[1] + "-" + dataNaixLlista[0];


            if (reserva.getEsmorzar()) {
                System.out.print("Aquest hoste té al·lèrgies? (si/no): ");

                while (true) {
                    String resposta = introduit.nextLine();
                    if (resposta.equalsIgnoreCase("si")) {
                        alergiesFinals = true;
                        break;
                    } else if (resposta.equalsIgnoreCase("no")) {
                        alergiesFinals = false;
                        break;
                    } else {
                        System.out.print("ERROR! Resposta incorrecta. Escriu 'si' o 'no': ");
                    }
                }
            }

            hostes.add(new Hostes(nomFinal, dniFinal, dataNaixOrdenada, alergiesFinals));
        }

        System.out.println("------------------------------------------------------");

        return hostes;
    }

    public static void imprimirReserva(ReservaBona reserva, UsuariBo usuariBo, List<Hostes> hostes){
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("Resum de la reserva a nom de: " + usuariBo.getNom());
        System.out.println("------------------------------------------------------");
        System.out.println("Data d'entrada: " + reserva.getDataEntrada());
        System.out.println("Data de sortida: " + reserva.getDataSortida());
        System.out.println("Total de nits: " + reserva.getNits() + " nits");
        System.out.println("Total d'hostes: " + reserva.getPersones() + " hostes");
        System.out.println();

        System.out.println("--- Dades de contacte de la reserva ---");
        System.out.println("Telèfon: " + usuariBo.getTelefon());
        System.out.println("Correu electrònic: " + usuariBo.getCorreu());
        System.out.println();

        for(int i = 0; i < reserva.getPersones(); i++){
            System.out.println("------------------------------------------------------");
            System.out.println("-- Dades de la persona " + (i+1) + " --");
            System.out.println("Nom: " + reserva.getNomHosta(hostes.get(i)));
            System.out.println("DNI: " + reserva.getDniHosta(hostes.get(i)));
            System.out.println("Data de naixement: " + reserva.getdataNaixementHosta(hostes.get(i)));

            if(reserva.getEsmorzar()){
                if(reserva.getAlergiesHosta(hostes.get(i))){
                    System.out.println("Aquest hoste té al·lèrgies.");
                } else {
                    System.out.println("Aquest hoste no té al·lèrgies.");
                }
            }
            System.out.println();
        }

        System.out.println("------------------------------------------------------");
        System.out.println("Total: " + String.format("%.2f", reserva.getPreu()) + "€");
        System.out.println("------------------------------------------------------");
    }

}