package com.example.bilet1.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bilet1.Citat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkManager extends AsyncTask<String, Void, List<Citat>> {
    private URL url;
    private HttpURLConnection connection;
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    private List<Citat> parseJson(String json) {
        if(json == null) {
            Log.d("RULA", "aici0");
            return null;
        }
        Log.d("RULA", "aici");
        List<Citat> citate = new ArrayList<>();

        try {
            Log.d("RULA", "aici2");
            JSONArray array = new JSONArray(json);
            Log.d("RULA", "aici3");
            for(int i = 0; i < array.length(); i++) {
                citate.add(getCitat(array.getJSONObject(i)));
                Log.d("RULA",getCitat(array.getJSONObject(i)).toString() );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return citate;

    }

    private Citat getCitat(JSONObject object) throws JSONException {
        String autor = object.getString("autor");
        String text=object.getString("text");
        Integer numarAprecieri = object.getInt("numarAprecieri");
        String categorie = object.getString("categorie");

        return new Citat(autor,text,numarAprecieri,categorie);
    }


    @Override
    protected List<Citat> doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();

        try {
            url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            inputStream = connection.getInputStream();
            inputStreamReader= new InputStreamReader(inputStream);
            Log.d("RULA", "aiciiii");
            bufferedReader= new BufferedReader(inputStreamReader);

            String linie;
            while ((linie = bufferedReader.readLine())!=null) {
                result.append(linie);
                Log.d("RULA", "aici5");
            }

            Log.d("RULA", "aici4");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJson(result.toString());

    }
}
