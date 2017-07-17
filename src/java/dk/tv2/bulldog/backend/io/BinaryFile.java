package dk.tv2.bulldog.backend.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author migo
 */
public class BinaryFile {

    private final File file;
    private String base64;
    private String md5;

    public BinaryFile(File file) {
        this.file = file;
        generateBase64AndMD5();
    }

    private void generateBase64AndMD5() {
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        byte[] bytes = new byte[2048];
        int read = 0;
        try (InputStream inputStream = new FileInputStream(file)) {
            while (read != -1) {
                read = inputStream.read(bytes);
                if (read != -1) {
                    byteBuffer.put(bytes, 0, read);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.toString());
        }
        base64 = Base64.getEncoder().encodeToString(byteBuffer.array());

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md5 = DatatypeConverter.printHexBinary(md.digest(byteBuffer.array())).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BinaryFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getMD5() {
        return md5;
    }

    public String getBase64() {
        return base64;
    }

}
