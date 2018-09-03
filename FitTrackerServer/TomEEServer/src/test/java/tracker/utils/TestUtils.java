package tracker.utils;

import org.json.JSONObject;

import java.io.*;

public class TestUtils {

    public static void writeTokens(String refreshToken, String accessToken) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/user_tokens.json"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("refresh_token", refreshToken);
            jsonObject.put("access_token", accessToken);
            writer.write(jsonObject.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readStream(InputStream is)
    {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
