import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Arrays;

/**
 * @autor Raúl Varandela Marra
 * Fecha: 16/10/2020
 */

public class DesempaquetarExamen {
    public static void main(String[] args) throws Exception {
        if (args.length != 5) {
            System.out.println("Faltan argumentos, el formato debe de ser el siguiente: ");
            System.out.println("DesempaquetarExamen <nombre paquete> <fichero examen> <Clave privada profesor> <Clave publica alumno> <Clace publica autoridad>");
            System.exit(1);
        }

        /**************************************************
         * Comprobación de los datos
         **************************************************/

        //Recuperación del paquete de disco y extracción de los datos
        Paquete paqueteSellado = PaqueteDAO.leerPaquete(args[0]);
        byte[] claveCifrada = paqueteSellado.getContenidoBloque(paqueteSellado.getNombresBloque().get(0));
        byte[] examenCifrado = paqueteSellado.getContenidoBloque(paqueteSellado.getNombresBloque().get(1));
        byte[] fecha = paqueteSellado.getContenidoBloque(paqueteSellado.getNombresBloque().get(2));
        byte[] firmaCifrada = paqueteSellado.getContenidoBloque(paqueteSellado.getNombresBloque().get(3));
        byte[] selloCifrado = paqueteSellado.getContenidoBloque(paqueteSellado.getNombresBloque().get(4));


        /**
         * comprobar los datos del alumno
         */

        //se descifra la firma
        Security.addProvider(new BouncyCastleProvider());
        Cipher cifrador = Cipher.getInstance("RSA", "BC");
        PublicKey publicaAlumno = PublicKeyReader.get(args[3]); //se recupera la clave de disco
        cifrador.init(Cipher.DECRYPT_MODE, publicaAlumno); //el cifrador se pone en modo descifrado
        byte[] resumenEnviadoAlumno = cifrador.doFinal(firmaCifrada); //se descifra la firma


        //se hace el HASH de examen y de la clave secreta
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(examenCifrado);
        messageDigest.update(claveCifrada);
        byte[] resumenRecibidoAlumno = messageDigest.digest();


        //comprobación
        if (Arrays.equals(resumenRecibidoAlumno, resumenEnviadoAlumno)) {
            System.out.println("PROFESOR: Los resumenes del alumno coinciden, todo correcto :)");
        } else {
            System.out.println("PROFESOR: Los resumenes del alumno no coinciden :(");
            System.exit(1);
        }

        /**
         * comprobar los datos autoridad
         */

        //descifrar el sello
        PublicKey publicaAutoridad = PublicKeyReader.get(args[4]);
        cifrador.init(Cipher.DECRYPT_MODE, publicaAutoridad); //el cifrador se pone en modo descifrado
        byte[] resumenEnviadoAutoridad = cifrador.doFinal(selloCifrado); //se descifra la firma


        //hash
        messageDigest.update(fecha);
        messageDigest.update(examenCifrado);
        messageDigest.update(claveCifrada);
        messageDigest.update(firmaCifrada);
        byte[] resumenRecibidoAutoridad = messageDigest.digest();


        //comprobacion
        if (Arrays.equals(resumenRecibidoAutoridad, resumenEnviadoAutoridad)) {
            System.out.println("PROFESOR: Los resumenes de la autoridad coinciden, todo correcto :)");
            System.out.print("PROFESOR: La fecha del sellado es: ");
            mostrarBytes(fecha);
        } else {
            System.out.println("PROFESOR: Los resumenes de la autoridad no coinciden :(");
            System.exit(1);
        }

        /**************************************************
         * Obtener los datos
         **************************************************/


        //descifrar la clave
        PrivateKey privadaProfesor = PrivateKeyReader.get(args[2]);
        cifrador.init(Cipher.DECRYPT_MODE, privadaProfesor); // Descrifra con la clave privada
        byte[] temp = cifrador.doFinal(claveCifrada);
        SecretKey clave = new SecretKeySpec(temp, 0, temp.length, "DES");


        //descifrar el examen
        FileOutputStream out = new FileOutputStream(args[1]);
        Cipher cifradorDES = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cifradorDES.init(Cipher.DECRYPT_MODE, clave);
        byte[] examen = cifradorDES.doFinal(examenCifrado);
        out.write(examen);
        out.close();

    }

    private static void mostrarBytes(byte[] buffer) {
        System.out.write(buffer, 0, buffer.length);
    }
}
