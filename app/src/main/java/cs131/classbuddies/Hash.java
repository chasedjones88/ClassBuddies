package cs131.classbuddies;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Hash {

    static String hashString(String stringToHash){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(stringToHash.getBytes());
            StringBuilder digestString = new StringBuilder ( );
            for (byte aDigest : digest) {
                digestString.append (Integer.toString ((aDigest & 0xff) + 0x100, 16).substring (1));
            }
            return digestString.toString ( );
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
