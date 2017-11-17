package cgodin.qc.ca.androidrestaurant.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.model.Restaurant;

/**
 * Created by Mohamed on 15/11/2017.
 */

public class JSONGetRequest extends AsyncTask<String, String, JSONObject> {

    //Probleme!!! ---->
    public static ArrayList<Restaurant> lstRestaurants = new ArrayList<Restaurant>();
    private Context context;

    String API_KEY = "AIzaSyABNpwVRjWbPr8zHc5-ZKa8yuLffZmVKKE";
    String RADIUS = "5000";
    String LATITUDE = "";
    String LONGTITUDE = "";

    // LocationService
    private FusedLocationProviderClient mFusedLocationClient;

    LocationTrack locationTrack;

    public JSONGetRequest(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //lstRestaurants.clear();

        //lstRestaurants = new ArrayList<Restaurant>();



        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            locationTrack = new LocationTrack(context);


            if (locationTrack.canGetLocation()) {

            /*double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();*/

                LONGTITUDE = Double.toString(locationTrack.getLongitude());
                LATITUDE = Double.toString(locationTrack.getLatitude());

                Log.e("JSONGetRequest ", "Latitude:" + LATITUDE + "\nLongitude: " + LONGTITUDE);
            } else {

                locationTrack.showSettingsAlert();
            }
        }

        locationTrack = new LocationTrack(context);


        if (locationTrack.canGetLocation()) {

            /*double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();*/

            LONGTITUDE = Double.toString(locationTrack.getLongitude());
            LATITUDE = Double.toString(locationTrack.getLatitude());

            //Toast.makeText(context, "Latitude:" + LATITUDE + "\nLongitude: " + LONGTITUDE, Toast.LENGTH_SHORT).show();

            Log.e("JSONGetRequest ", "Latitude:" + LATITUDE + "\nLongitude: " + LONGTITUDE);
        } else {

            locationTrack.showSettingsAlert();
        }

    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject jsonObj = null;
        HttpURLConnection con = null;

        try {

            // Creating a Request
            URL urlPlaceSearch = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurant&location=" + LATITUDE + "," + LONGTITUDE + "&radius=" + RADIUS + "&key=" + API_KEY);
            con = (HttpURLConnection) urlPlaceSearch.openConnection();
            con.setRequestMethod("GET");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.flush();
            out.close();

            // Reading the Response
            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            jsonObj = new JSONObject(content.toString());

            JsonParser parser = new JsonParser();

            JsonElement placeSearchJsonResult = parser.parse(content.toString());

            if(placeSearchJsonResult.isJsonObject()) {
                JsonObject jsonObject = placeSearchJsonResult.getAsJsonObject();

                JsonElement resultsJson = jsonObject.get("results");


                if (resultsJson.isJsonArray()) {
                    JsonArray resultsJsonArray = resultsJson.getAsJsonArray();


                    for (int i = 0; i < resultsJsonArray.size(); i++) {

                        JsonObject restaurantJsonObject = resultsJsonArray.get(i).getAsJsonObject();

                        String formatted_address = restaurantJsonObject.get("formatted_address").getAsString();
                        String name = restaurantJsonObject.get("name").getAsString();
                        double rating = -1;
                        if (restaurantJsonObject.has("rating")){
                            rating = restaurantJsonObject.get("rating").getAsDouble();
                        }
                        String place_id = restaurantJsonObject.get("place_id").getAsString();
                        String photo_reference = "";

                        if (restaurantJsonObject.has("photos")){
                            photo_reference = restaurantJsonObject.get("photos").getAsJsonArray().get(0).getAsJsonObject().get("photo_reference").getAsString();
                        }

                        URL urlPlaceDetails = new URL("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + API_KEY);

                        con = (HttpURLConnection) urlPlaceDetails.openConnection();
                        con.setRequestMethod("GET");

                        con.setDoOutput(true);
                        DataOutputStream outPlaceDetails = new DataOutputStream(con.getOutputStream());

                        outPlaceDetails.flush();
                        outPlaceDetails.close();

                        // Reading the Response
                        int status2 = con.getResponseCode();

                        BufferedReader in2 = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine2;
                        StringBuffer content2 = new StringBuffer();
                        while ((inputLine2 = in2.readLine()) != null) {
                            content2.append(inputLine2);
                        }
                        in2.close();

                        JSONObject jsonObj2 = new JSONObject(content2.toString());

                        JsonObject placeDetailsJson = parser.parse(content2.toString()).getAsJsonObject().get("result").getAsJsonObject();

                        String place_url = placeDetailsJson.get("url").getAsString();

                        String formatted_phone_number = "";
                        if (placeDetailsJson.has("formatted_phone_number")){
                            formatted_phone_number = placeDetailsJson.get("formatted_phone_number").getAsString();
                        }

                        String img_api_url = "";
                        if (photo_reference != ""){
                            img_api_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo_reference + "&key=" + API_KEY;
                        }

                        Restaurant restaurant = new Restaurant(formatted_address,name,rating,place_id,photo_reference,img_api_url,place_url,formatted_phone_number);

                        lstRestaurants.add(restaurant);

                    }
                }
            }

            con.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        Log.e("OnPostExecute", "size: " + lstRestaurants.size());

        for (int i = 0; i < lstRestaurants.size(); i++){
            Log.e("JSONGetRequest ", "Restaurant nbr: " + i + " " + lstRestaurants.get(i));
        }


    }

    public ArrayList<Restaurant> getRestaurantArrayList(){
        return lstRestaurants;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String getRADIUS() {
        return RADIUS;
    }

    public void setRADIUS(String RADIUS) {
        this.RADIUS = RADIUS;
    }

    public String getLONGTITUDE() {
        return LONGTITUDE;
    }

    public void setLONGTITUDE(String LONGTITUDE) {
        this.LONGTITUDE = LONGTITUDE;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

}
