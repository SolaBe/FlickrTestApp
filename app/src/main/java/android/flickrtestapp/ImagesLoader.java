package android.flickrtestapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sola2Be on 12.09.2015.
 */
public class ImagesLoader extends AsyncTaskLoader {

    private String text = "&text=";
    private static final String secret = "80088bf53d5dfc79";
    private static final String api_key = "7884cc8e6d4cb189449825f4151462d7"; // use only for api_sig create
    private static final String key = "&api_key=7884cc8e6d4cb189449825f4151462d7";
    private static final String base_url = "https://api.flickr.com/services/rest/?method=";
    private static final String search_method = "flickr.photos.search";
    private static final String get_sizes_method = "flickr.photos.getSizes";
    private static final String addit_params = "&per_page=20&format=json";
    private static final String api_sig_not_md5 = secret+"api_key"+api_key+"permsread"; // not use

    public ImagesLoader(Context context, String query) {
        super(context);
        text = text+query;
    }

    @Override
    public List loadInBackground() {
       // String apiSig = "&api_sig="+createMD5(api_sig_not_md5);
        String urlSearchStrign  = base_url+search_method+key+text+addit_params;//+apiSig;
        List<ModelPhoto> photoList = new ArrayList<>();
        List<Bitmap> bitmapList = new ArrayList<Bitmap>();
        Bitmap bmp;
        try {
            String json = getJsonFromConnection(urlSearchStrign);
            JSONObject jsObj = new JSONObject(json.replace("jsonFlickrApi(", "").replace(")", ""));
            JSONObject photos = jsObj.getJSONObject("photos");
            JSONArray jsonArray = photos.getJSONArray("photo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                photoList.add(new ModelPhoto(item.getString("id")));
            }
            for(ModelPhoto model : photoList) {
                String urlSizesString = base_url + get_sizes_method + key + "&photo_id=" + model.getId() + addit_params;// + apiSig;
                json = getJsonFromConnection(urlSizesString);
                jsObj = new JSONObject(json.replace("jsonFlickrApi(", "").replace(")", ""));
                JSONObject sizes = jsObj.getJSONObject("sizes");
                JSONArray size = sizes.getJSONArray("size");
                for (int i = 0; i < size.length(); i++) {
                    JSONObject image = size.getJSONObject(i);
                    if (image.getString("label").equals("Medium")){
                        model.setMediumURL(image.getString("source"));
                        bmp = BitmapFactory.decodeStream(getIsFromConnection(image.getString("source")));
                        bitmapList.add(bmp);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bitmapList;
    }



    public static final String createMD5(final String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public InputStream getIsFromConnection(String urlString){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(15000);
                return urlConnection.getInputStream();
//                Log.d("is avaliable", "="+is.available() );
//                bis = new BufferedInputStream(urlConnection.getInputStream());
                //urlConnection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getJsonFromConnection(String urlString){
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(15000);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    sb.append(inputLine);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            urlConnection.disconnect();
        }
        return sb.toString();
    }

//    public ByteArrayOutputStream getBaos(BufferedInputStream bis){
//        int size = 1024;
//        byte[] buffer = new byte[size];
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int read = 0;
//        try {
//            while ((read = bis.read(buffer)) != -1) {
//                if (read > 0) {
//                    baos.write(buffer, 0, read);
//                    buffer = new byte[size];
//                }
//
//            }
//        } catch (IOException e) {
//            try {
//                bis.close();
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        }
//        return baos;
//    }
}
