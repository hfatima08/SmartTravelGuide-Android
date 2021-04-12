package aptech.fatima.map;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class WeatherFragment extends Fragment {
    SearchView weather;
    ImageView imageView;
    RelativeLayout layout1,layout2;
    LatLng dest;
    String loc;
    String tour;
    TextView country,city,temp,date,humidity,latitude,longitude,pressure,wind,desc,like;
    public WeatherFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weather, container, false);
        weather=view.findViewById(R.id.W_search);
        country=view.findViewById(R.id.country);
        city=view.findViewById(R.id.city);
        temp=view.findViewById(R.id.temp);
        date=view.findViewById(R.id.date);
        humidity=view.findViewById(R.id.humidity2);
        latitude=view.findViewById(R.id.latitude2);
        longitude=view.findViewById(R.id.longitude2);
        pressure=view.findViewById(R.id.pressure2);
        like=view.findViewById(R.id.like);
        desc=view.findViewById(R.id.description);
        wind=view.findViewById(R.id.wind2);
        imageView=view.findViewById(R.id.weather);
        layout1=view.findViewById(R.id.r1);
        layout2=view.findViewById(R.id.r2);

       tour=getArguments().getString("data");
       if(tour!=null) {
           setDest(tour);
       }
            weather.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    loc = weather.getQuery().toString();
                    setDest(loc);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    return false;
                }
            });
        return view;
    }

    public void setDest(String loca){
        List<Address> addressList = null;
        if (loca != null || !loca.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(loca, 1);
                if (!addressList.isEmpty()) {
                    Address address = addressList.get(0);
                    dest = new LatLng(address.getLatitude(), address.getLongitude());
                    findWeather(dest,loca);
                } else {
                    Toast.makeText(getContext(), "Enter a City Name", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void findWeather(LatLng desti,String loc_name) {
        final double lat=desti.latitude;
       final  double lon=desti.longitude;
       final String location=loc_name;


        String url="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=753bcbd2f56deb805c0cd1a22dc4f8e0";

        StringRequest stringReq= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);



                    layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.VISIBLE);
                    //temp
                    JSONObject object2 = jsonObject.getJSONObject("main");
                    double temp_find = object2.getDouble("temp");
                    int temperature = (int) (temp_find - 273.15);

                    if(temperature!=0){
                        temp.setVisibility(View.VISIBLE);
                        temp.setText(temperature + " °C");}

                    //icon
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    JSONObject object3 = jsonArray.getJSONObject(0);
                    String img = object3.getString("icon");
                    if(img!=null || !img.equals("")){
                        imageView.setVisibility(View.VISIBLE);
                    Picasso.get().load("http://openweathermap.org/img/wn/" + img + "@2x.png").into(imageView);
                        }

                    //description
                    JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                    JSONObject object9 = jsonArray1.getJSONObject(0);
                    String des_find = object9.getString("description");
                    if(des_find!=null || !des_find.equals("")){
                        desc.setVisibility(View.VISIBLE);
                         desc.setText(des_find);}

                    //feels Like
                    JSONObject object10 = jsonObject.getJSONObject("main");
                    double feel_like = object10.getDouble("feels_like");
                    int temperature2 = (int) (feel_like - 273.15);
                    if(temperature2!=0){
                        like.setVisibility(View.VISIBLE);
                    like.setText("Feels Like: " + temperature2 + " °C");}

                    //date & time
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat std = new SimpleDateFormat("dd/MM/yyyy \n HH:mm:ss");
                    String datetime = std.format(calendar.getTime());
                    if(datetime!=null || !datetime.equals("")){
                        date.setVisibility(View.VISIBLE);
                    date.setText(datetime);}

                    //humidity
                    JSONObject object4 = jsonObject.getJSONObject("main");
                    String humidity_find = object4.getString("humidity");
                    if(humidity_find!=null || !humidity_find.equals("")){
                        humidity.setVisibility(View.VISIBLE);
                    humidity.setText(humidity_find + " %");}

                    //sunrise
                    JSONObject object5 = jsonObject.getJSONObject("coord");
                    String lat_find = object5.getString("lat");
                    if(lat_find!=null || !lat_find.equals("")){
                        latitude.setVisibility(View.VISIBLE);
                    latitude.setText(lat_find + " °N");}

                    //sunset
                    JSONObject object6 = jsonObject.getJSONObject("coord");
                    String long_find = object6.getString("lon");
                    if(long_find!=null || !long_find.equals("")) {
                        longitude.setVisibility(View.VISIBLE);
                        longitude.setText(long_find + " °N");
                    }
                    //sunrise
                    JSONObject object7 = jsonObject.getJSONObject("main");
                    String pressure_find = object7.getString("pressure");
                    if(pressure_find!=null || !pressure_find.equals("")){
                        pressure.setVisibility(View.VISIBLE);
                    pressure.setText(pressure_find + " hPa");}


                    //sunrise
                    JSONObject object8 = jsonObject.getJSONObject("wind");
                    String wind_find = object8.getString("speed");
                    if(wind_find!=null || !wind_find.equals("")){
                        wind.setVisibility(View.VISIBLE);
                    wind.setText(wind_find + " km/h");}


                        country.setVisibility(View.VISIBLE);
                        country.setText(location);
                        city.setText("");

                    //country
                    JSONObject object1 = jsonObject.getJSONObject("sys");
                    String country_find = object1.getString("country");
                    if(country_find !=null || !country_find.equals("")) {
                        city.setVisibility(View.VISIBLE);
                        city.setText(country_find);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringReq);
        weather.setQuery("",false);
    }
}