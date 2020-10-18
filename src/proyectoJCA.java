/**
 * @autor Raúl Varandela Marra
 * Fecha: 16/10/2020
 */


public class proyectoJCA {
    public static void main(String[] args) throws Exception {


        /**************************************************
         * Generaciñon de claves
         **************************************************/

        //generación de claves para el alumno
        String[] ruta_alumno = new String[1];
        ruta_alumno[0] = "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno";
        GenerarClaves.main(ruta_alumno);

        //generación de claves para el profesor
        String[] ruta_profesor = new String[1];
        ruta_profesor[0] = "C:\\Users\\Raul\\Documents\\claves\\profesor\\profesor";
        GenerarClaves.main(ruta_profesor);

        //generación de claves para el autoridad
        String[] ruta_autoridad = new String[1];
        ruta_autoridad[0] = "C:\\Users\\Raul\\Documents\\claves\\autoridad\\autoridad";
        GenerarClaves.main(ruta_autoridad);

        System.out.println();


        /**************************************************
         * Empaquetado del Exmaen
         **************************************************/

        String[] argEmpaquetarExamen = new String[4];
        argEmpaquetarExamen[0] = "C:\\Users\\Raul\\Documents\\examen.txt";
        argEmpaquetarExamen[1] = "C:\\Users\\Raul\\Desktop\\examenEmpaquetado.bin";
        argEmpaquetarExamen[2] = "C:\\Users\\Raul\\Documents\\claves\\profesor\\profesor.publica";
        argEmpaquetarExamen[3] = "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno.privada";
        EmpaquetarExamen.main(argEmpaquetarExamen);


        /**************************************************
         * Sellado del examen
         **************************************************/

        String[] argSellarExamen = new String[3];
        argSellarExamen[0] = "C:\\Users\\Raul\\Desktop\\examenEmpaquetado.bin";
        argSellarExamen[1] = "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno.publica";
        argSellarExamen[2] = "C:\\Users\\Raul\\Documents\\claves\\autoridad\\autoridad.privada";
        SellarExamen.main(argSellarExamen);

        /**************************************************
         * Desempaquetado del Exmaen
         **************************************************/

        String[] argDesempaquetarExamen = new String[5];
        argDesempaquetarExamen[0] = "C:\\Users\\Raul\\Desktop\\examenEmpaquetado.bin";
        argDesempaquetarExamen[1] = "C:\\Users\\Raul\\Desktop\\examen_en_claro.txt";
        argDesempaquetarExamen[2] = "C:\\Users\\Raul\\Documents\\claves\\profesor\\profesor.privada";
        argDesempaquetarExamen[3] = "C:\\Users\\Raul\\Documents\\claves\\alumno\\alumno.publica";
        argDesempaquetarExamen[4] = "C:\\Users\\Raul\\Documents\\claves\\autoridad\\autoridad.publica";
        DesempaquetarExamen.main(argDesempaquetarExamen);

    }
}
