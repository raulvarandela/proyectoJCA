/**
@autor Raúl Varandela Marra
 Fecha: 16/10/2020
*/



public class proyectoJCA {
    public static void main(String[] args) throws Exception {

        //generación de claves para el alumno
        String[] ruta_alumno = new String[1];
        ruta_alumno[0]= "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno";
        GenerarClaves.main(ruta_alumno);

        //generación de claves para el profesor
        String[] ruta_profesor = new String[1];
        ruta_profesor[0]= "C:\\Users\\Raul\\Documents\\claves\\profesor\\profesor";
        GenerarClaves.main(ruta_profesor);

        //generación de claves para el autoridad
        String[] ruta_autoridad = new String[1];
        ruta_autoridad[0]= "C:\\Users\\Raul\\Documents\\claves\\autoridad\\profesor";
        GenerarClaves.main(ruta_autoridad);

        String[] argEmpaquetarExamen = new String[4];
        argEmpaquetarExamen[0] = "C:\\Users\\Raul\\Documents\\examen.txt";
        argEmpaquetarExamen[1] = "C:\\Users\\Raul\\Desktop\\examenEmpaquetado.bin";
        argEmpaquetarExamen[2] = "C:\\Users\\Raul\\Documents\\claves\\autoridad\\profesor.publica";
        argEmpaquetarExamen[3] = "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno.privada";
        EmpaquetarExamen.main(argEmpaquetarExamen);

    }
}
