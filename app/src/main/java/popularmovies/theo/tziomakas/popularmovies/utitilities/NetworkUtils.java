package popularmovies.theo.tziomakas.popularmovies.utitilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import popularmovies.theo.tziomakas.popularmovies.model.Movies;
import popularmovies.theo.tziomakas.popularmovies.model.Reviews;
import popularmovies.theo.tziomakas.popularmovies.model.Trailers;

/**
 * Created by theodosiostziomakas on 29/12/2017.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_185 = "w185";
    private static final String IMAGE_NOT_FOUND = "http://i.imgur.com/N9FgF7M.png";
    private static final String PATH_SEPARATOR = "/";



    public NetworkUtils(){

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createURL(String stringURL){

        URL url = null;

        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        //Log.v(LOG_TAG,jsonResponse);
        return jsonResponse;
    }

    /**
     * Return a list of {@link Movies} objects that has been built up from
     * parsing the given JSON response.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    private static List<Movies> extractMoviesFromJson(String movieJson){
        if(TextUtils.isEmpty(movieJson)){
            return null;
        }

        List<Movies> movies = new ArrayList<>();

        String movieId;
        String title;
        Double rating;
        String overview;
        String image;
        String date;

        try {
            JSONObject baseJSON = new JSONObject(movieJson);

            JSONArray resultArray = baseJSON.getJSONArray("results");

            for(int i = 0; i < resultArray.length(); i++){

                JSONObject item = resultArray.getJSONObject(i);

                movieId = item.getString("id");

                title = item.getString("title");

                overview = item.getString("overview");

                if(item.getString("poster_path")==null){
                    image = IMAGE_NOT_FOUND;
                }else{
                    image = item.getString("poster_path");
                }
                rating = item.getDouble("vote_average");

                date = item.getString("release_date");

                Movies m = new Movies(movieId,title,image,overview,rating,date);

                movies.add(m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;

    }

    /**
     * Extracting trailers from the json response  and return a list of {@link Trailers} objects.
     */
    private static List<Trailers> extractTrailersFromJson(String url){
        List<Trailers> trailersList = new ArrayList<>();

        final String MOVIE_ID = "id";
        final String TRAILER_ID= "id";

        final String RESULTS = "results";
        final String KEY = "key";
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";

        try {
            JSONObject trailersJson = new JSONObject(url);

            String movieId = trailersJson.getString(MOVIE_ID);

            JSONArray trailersArray = trailersJson.getJSONArray(RESULTS);

            for(int i = 0; i<trailersArray.length(); i++){
                JSONObject trailerJSONObject = trailersArray.getJSONObject(i);


                String trailerId = trailerJSONObject.getString(TRAILER_ID);
                String key = trailerJSONObject.getString(KEY);
                String name = trailerJSONObject.getString(NAME);
                String site =  trailerJSONObject.getString(SITE);
                String type = trailerJSONObject.getString(TYPE);

                Trailers trailers = new Trailers(movieId,trailerId,key,name,site,type);

                trailersList.add(trailers);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersList;

    }

    /**
     * Extracting reviews from the json response and return a list of {@link Reviews} objects.
     */
    private static ArrayList<Reviews> extractReviewsFromJson(String url){
        ArrayList<Reviews> reviewsList = new ArrayList<>();

        final String MOVIE_ID = "id";
        final String REVIEW_ID= "id";

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL_OF_REVIEW = "url";


        try {
            JSONObject reviewsJson = new JSONObject(url);

            String movieId = reviewsJson.getString(MOVIE_ID);

            JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);

            for(int i = 0; i<reviewsArray.length(); i++){
                JSONObject reviewJSONObject = reviewsArray.getJSONObject(i);

                String trailerId = reviewJSONObject.getString(REVIEW_ID);
                String author = reviewJSONObject.getString(AUTHOR);
                String content = reviewJSONObject.getString(CONTENT);

                String urlReview =  reviewJSONObject.getString(URL_OF_REVIEW);

                Reviews reviews = new Reviews(movieId,trailerId,author,content,urlReview);

                reviewsList.add(reviews);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsList;

    }

    /**
     * Query the dataset and return a list of {@link Movies} objects.
     */
    public static List<Movies> fetchMoviesData(String requestUrl){
        URL url = createURL(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Movies> movies = extractMoviesFromJson(jsonResponse);

        return movies;
    }

    /**
     * Query the dataset and return a list of {@link Trailers} objects.
     */
    public static List<Trailers> fetchTrailersData(String requestUrl){
        URL url = createURL(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Trailers> trailers = extractTrailersFromJson(jsonResponse);

        return trailers;
    }

    /**
     * Query the dataset and return a list of {@link Reviews} objects.
     */
    public static ArrayList<Reviews> fetchReviewData(String requestUrl){
        URL url = createURL(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Reviews> reviews = extractReviewsFromJson(jsonResponse);

        return reviews;
    }

    public static String getImageURL(String imagePath) {

        StringBuilder imageURL = new StringBuilder();

        imageURL.append(IMAGE_URL);
        imageURL.append(IMAGE_SIZE_185);
        imageURL.append(PATH_SEPARATOR);
        imageURL.append(imagePath);

        return imageURL.toString();
    }
}
