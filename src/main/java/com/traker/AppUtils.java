package main.java.com.traker;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AppUtils
{
    public static String inputStreamToString(InputStream is)
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

    public static JSONObject inputStreamToJson(InputStream is)
    {
        return new JSONObject(inputStreamToString(is));
    }
}
