import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.time.LocalDate;
import java.util.Arrays;


/**
 * @autor Raúl Varandela Marra
 * Fecha: 16/10/2020
 */

public class SellarExamen {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Faltan argumentos, el formato debe de ser el siguiente: ");
            System.out.println("SellarExamen <nombre paquete> <Clave publica alumno> <Clave privada autoridad>");
            System.exit(1);
        }


        /**************************************************
         * Verificar firma
         **************************************************/

        /**
         * Primero recuperaremos el resumen enviado
         */

        Paquete examenEmpaquetado = PaqueteDAO.leerPaquete(args[0]); //se lee el paquete de disco
        byte[] firma = examenEmpaquetado.getContenidoBloque(examenEmpaquetado.getNombresBloque().get(2)); //se recupera la frima del paquete

        //se configura el cifrador RSA
        Security.addProvider(new BouncyCastleProvider());
        Cipher cifrador = Cipher.getInstance("RSA", "BC");
        PublicKey publicaAlumno = PublicKeyReader.get(args[1]); //se recupera la clave de disco
        cifrador.init(Cipher.DECRYPT_MODE, publicaAlumno); //el cifrador se pone en modo descifrado
        byte[] resumenEnviado = cifrador.doFinal(firma); //se descifra la firma


        /**
         * Ahora recuperamos el resumen recibido
         */

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] claveCifrada = examenEmpaquetado.getContenidoBloque(examenEmpaquetado.getNombresBloque().get(0)); // se recupera la calve
        byte[] examenCifrado = examenEmpaquetado.getContenidoBloque(examenEmpaquetado.getNombresBloque().get(1)); //se recupera el examen del paquete


        messageDigest.update(examenCifrado);
        messageDigest.update(claveCifrada);


        byte[] resumenRecibido = messageDigest.digest();


        //comparamos los resumenes
        if (Arrays.equals(resumenEnviado, resumenRecibido)) {
            System.out.println("AUTORIDAD: Los resumenes coinciden, todo correcto :)");
        } else {
            System.out.println("AUTORIDAD: Los resumenes no coinciden :(");
            System.exit(1);
        }

        /**************************************************
         * Sellado de tiempo
         **************************************************/

        //obtener la fecha
        LocalDate fecha = LocalDate.now();
        byte[] fechaActual = fecha.toString().getBytes();

        //hash de fecha,examen,clave y firma (en ese orden)
        messageDigest.update(fechaActual);
        messageDigest.update(examenCifrado);
        messageDigest.update(claveCifrada);
        messageDigest.update(firma);

        byte[] sello = messageDigest.digest();

        //se cifrar con RSA
        PrivateKey privadaAutoridad = PrivateKeyReader.get(args[2]);
        cifrador.init(Cipher.ENCRYPT_MODE, privadaAutoridad);
        byte[] selloCifrado = cifrador.doFinal(sello);

        //se añade al paquete el nuevo contenido
        examenEmpaquetado.anadirBloque("Fecha", fechaActual);
        examenEmpaquetado.anadirBloque("Sello", selloCifrado);
        PaqueteDAO.escribirPaquete(args[0], examenEmpaquetado);

    }

    private static void mostrarBytes(byte[] buffer) {
        System.out.write(buffer, 0, buffer.length);
    }


}
