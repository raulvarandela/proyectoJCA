/**
@autor Raúl Varandela Marra
 Fecha: 16/10/2020
*/



public class proyectoJCA {
    public static void main(String[] args) throws Exception {

        //generación de claves para el alumno
        String[] ruta_alumno = new String[1];
        ruta_alumno[0]= "C:\\Users\\Raul\\Desktop\\alumno\\alumno";
        GenerarClaves.main(ruta_alumno);

        //generación de claves para el profesor
        String[] ruta_profesor = new String[1];
        ruta_profesor[0]= "C:\\Users\\Raul\\Desktop\\profesor\\profesor ";
        GenerarClaves.main(ruta_profesor);

        //generación de claves para el autoridad
        String[] ruta_autoridad = new String[1];
        ruta_autoridad[0]= "C:\\Users\\Raul\\Desktop\\autoridad\\autoridad";
        GenerarClaves.main(ruta_autoridad);


        Paquete a = new Paquete();
        a.
    }
}
