
package Utils;

import java.util.Scanner;
public class UtilsHotel {
    public static int llegirInt(Scanner scanner, String s) {
        int numero;
        while(true){
            System.out.print(s);
            if(scanner.hasNextInt()){
                numero = scanner.nextInt();
                scanner.nextLine();
                return numero;
            }else{
                scanner.nextLine();
            }
        }
    }
}
