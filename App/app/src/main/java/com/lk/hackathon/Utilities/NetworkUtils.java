package com.lk.hackathon.Utilities;

import android.text.TextUtils;
import android.util.Log;

import com.lk.hackathon.Chapter;
import com.lk.hackathon.Notes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {

    private static String TAG="TAG";
    private static int MEGABYTE=1024*1024;


    public static List<Chapter> extractChapters(String requestURL){



        // Create URL object
        URL url = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.i(TAG,e.getLocalizedMessage());

        }
        return extractChaptersFromJSON(jsonResponse);
    }

    public static List<Chapter> extractChaptersFromJSON(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse))return null;
        List<Chapter> chapters=new ArrayList<>();
        String[] strArray1=jsonResponse.split(",");
        String id="";
        String name="";

        for (String str : strArray1) {
            String[] strArray2 = str.split(":");
            id = strArray2[0].replace(" ", "")
                                .replace("'","");
            name = strArray2[1].replace(" ", "")
                    .replace("'", "")
                    .replace("#",",");

//            Log.v("TAG","id: "+id);
//            Log.v("TAG","name: "+name);

            Chapter chapter=new Chapter(id,name);
            chapters.add(chapter);
        }
        return chapters;
    }

    public static List<Notes> extractNotes(String requestURL){
        // Create URL object
        URL url = createUrl(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.i(TAG,e.getLocalizedMessage());

        }
        List<Notes> articles=extractFeaturesFromJson(jsonResponse);
        Log.v(TAG,jsonResponse);
        return articles;
    }



    public static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
             Log.e(TAG, "Error with creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Response:  ", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
//            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.v(TAG,jsonResponse);
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Notes> extractFeaturesFromJson(String jsonResponse)  {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Notes> notes=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(jsonResponse);
            JSONArray notesArray=jsonObject.getJSONArray("notes");
            for(int i=0; i<notesArray.length(); i++){
                JSONObject object=notesArray.getJSONObject(i);
                String name=object.getString("name");
                String url=object.getString("link");
                String logo=object.getString("logo");
                String subject=object.getString("subject");
                int star=object.getInt("star");
                int id=object.getInt("id");
                int studentClass=object.getInt("class");
                Notes notes1=new Notes(id,name,url,logo,studentClass,subject,star);
                notes.add(notes1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notes;

    }

    public static void downloadFile(String fileUrl, File directory){
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
