/**
 * @autor Raúl Varandela Marra
 * Fecha: 16/10/2020
 * Descripción: Usado por el alumno para empaquetar el examen
 */


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import javax.crypto.*;
import java.io.*;

public class EmpaquetarExamen {


    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Faltan argumentos, el formato debe de ser el siguiente: ");
            System.out.println("EmpaquetarExamen <fichero examen> <nombre paquete> <Clave publica profesor> <Clave privada alumno>");
            System.exit(1);
        }


        /*************************************************************************
         * Cifrado del examen con DES
         *************************************************************************/

        //Generamos la clave alatoria para cifrar el examen con DES
        KeyGenerator generadorDES = KeyGenerator.getInstance("DES");
        generadorDES.init(56); // clave de 56 bits
        SecretKey clave = generadorDES.generateKey();

        //crear el cifrador
        Cipher cifrador = Cipher.getInstance("DES/ECB/PKCS5Padding");

        //poner el cifrador en modo cifrado
        cifrador.init(Cipher.ENCRYPT_MODE, clave);

        byte[] buffer;
        byte[] bufferCifrado;

        //leemos el examen


        buffer = Files.readAllBytes(Path.of(args[0]));

        bufferCifrado = cifrador.doFinal(buffer); // Completar cifrado



        /*************************************************************************
         * Cifrado de la clave secreta con RSA
         ************************************************************************/
        Security.addProvider(new BouncyCastleProvider());

        //se leen las claves de disco
        PrivateKey clavePrivadaAlumno = PrivateKeyReader.get(args[3]);
        PublicKey clavePublicaProfesor = PublicKeyReader.get(args[2]);


        //creo el cifrador
        Cipher cifradorRSA = Cipher.getInstance("RSA", "BC");
        byte[] entradaRSA = clave.getEncoded();
        cifradorRSA.init(Cipher.ENCRYPT_MODE, clavePublicaProfesor); //pongo en modo cifrar
        byte[] salidaRSA = cifradorRSA.doFinal(entradaRSA); //cifro


        /*************************************************************************
         * HASH de la clave y el examen y cifrado rsa del mismo
         ************************************************************************/

        //creamos el reumen
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bufferCifrado);
        messageDigest.update(salidaRSA);
        byte[] resumen = messageDigest.digest();

        //ciframos con RSA
        cifradorRSA.init(Cipher.ENCRYPT_MODE, clavePrivadaAlumno);
        byte[] firmaCifrada = cifradorRSA.doFinal(resumen);



        /*************************************************************************
         * Crear el paquete
         ************************************************************************/


        Paquete examenEmpaquetado = new Paquete();
        examenEmpaquetado.anadirBloque("Examen cifrado", bufferCifrado);
        examenEmpaquetado.anadirBloque("Clave secreta cifrada", salidaRSA);
        examenEmpaquetado.anadirBloque("Firma cifrada", firmaCifrada);
        PaqueteDAO.escribirPaquete(args[1], examenEmpaquetado);

        System.out.println("ALUMNO: El examen ha sido empaquetado");

    }

    private static void mostrarBytes(byte [] buffer) {
        System.out.write(buffer, 0, buffer.length);
    }

}
