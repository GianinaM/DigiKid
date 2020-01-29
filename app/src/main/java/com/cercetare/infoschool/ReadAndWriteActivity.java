package com.cercetare.infoschool;

import android.app.Activity;
import android.util.Log;

import com.cercetare.infoschool.model.Week;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Gianina on 5/6/2019.
 */

public class ReadAndWriteActivity extends Activity {

    @SuppressWarnings("unchecked")
    public void main()
    {
        String filename = "biologie.json";
        List<Week> weekdCourses = loadJSONFromAsset(filename);
    }

    public List<Week> loadJSONFromAsset(String filename){
        ArrayList<Week> weeksList = new ArrayList<>();
        String json = null;
        try {
            Log.println(Log.DEBUG, "readAndWrite", "All good");
            Log.println(Log.DEBUG, "readAndWrite", "filename: " + filename);
            Log.println(Log.DEBUG, "readAndWrite", "assets: " + getResources());
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("biologie");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                Log.i("ReadAndWrite", "inFOR");
//                MyLocations location = new MyLocations();
//                location.setLat((float) jo_inside.getDouble("lat"));
//                location.setLng((float) jo_inside.getDouble("lng"));
//                location.setKey(jo_inside.getString("key"));
//                location.setRadius(jo_inside.getInt("radius"));
//                location.setName(jo_inside.getString("name"));
//                location.setAudio_file(jo_inside.getString("audio_file"));


                //Add your values in your `ArrayList` as below:
//                locList.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weeksList;
    }
}
