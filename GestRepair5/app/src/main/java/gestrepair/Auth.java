package gestrepair;

import android.util.Base64;

import java.util.HashMap;

/**
 * Created by Obscu on 06/09/2017.
 */

public class Auth {
    String username, password;
    private HashMap<String, String> hashmap = new HashMap<String, String>();

    public HashMap<String, String> getHashmap() {
        String credentials = username + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + base64EncodedCredentials);
        return headers;
    }

    public void setHashmap(HashMap<String, String> hashmap) {
        this.hashmap = hashmap;
    }
}
