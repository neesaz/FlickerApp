package com.example.flickr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public JSONObject imageOne = new JSONObject();
    public JSONObject imageTwo = new JSONObject();
    public  int pageNumber = 1;
    private static int[] ids = {R.id.imageView, R.id.imageView2};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadJasonTask().execute();
        new DownloadImageTaskOne().execute();
        new DownloadImageTaskTwo().execute();
    }


    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;


        }
    }



    private class DownloadJasonTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Long doInBackground(String... strs) {
            try {
                URL myUrl = new URL("https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=c68268937007bf1cc8ac4be2d67276b0&extras=original_format,date_taken,url_c&per_page=5&page="+pageNumber+"&format=json&nojsoncallback=1");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myUrl.openStream()));
                JSONObject jsonObject = new JSONObject(bufferedReader.readLine());
                JSONArray photos = jsonObject.getJSONObject("photos").getJSONArray("photo");
                imageOne = photos.getJSONObject(0);
                imageTwo = photos.getJSONObject(1);
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } /** TODO url */
            return new Long(0);
        }


        @Override
        protected void onPostExecute(Long aLong) {
            TextView calandarInfoOne = findViewById(R.id.textView);
//            TextView fileInfoOne = findViewById(R.id.file1);
//            TextView tagInfoOne = findViewById(R.id.tag);
//            TextView calandarInfoTwo = findViewById(R.id.calnder2);
//            TextView fileInfoTwo = findViewById(R.id.file2);
//            TextView tagInfoTwo = findViewById(R.id.tag2);

            try {
                calandarInfoOne.setText(imageOne.getString("datetaken"));
//                fileInfoOne.setText(imageOne.getString("originalformat"));
//                tagInfoOne.setText(imageOne.getString("title"));
//                calandarInfoTwo.setText(imageTwo.getString("datetaken"));
//                fileInfoTwo.setText(imageTwo.getString("originalformat"));
//                tagInfoTwo.setText(imageTwo.getString("title"));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class DownloadImageTaskOne extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = null;
            try {
                url = imageOne.getString("url_c");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getBitmapFromURL(url);
        }

        protected void onPostExecute(Bitmap result) {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(result);
        }
    }


    private class DownloadImageTaskTwo extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = null;
            try {
                url = imageTwo.getString("url_c");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getBitmapFromURL(url);
        }

        protected void onPostExecute(Bitmap result) {
            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageBitmap(result);
        }
    }


}