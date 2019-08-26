package fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements LocationListener {
    public static final String TAG = MapFragment.class.getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationManager mLocationManager;
    private TextInputEditText txt_name,txt_mobile,txt_add1,txt_add2, txt_street_locality, txt_landmarkTxt, txt_pinTxt, txt_district;
    private Button isDeliverable,btnCheckout;
    String address="",prdid="";

    @TargetApi(Build.VERSION_CODES.P)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_layout, container, false);
        txt_name = (TextInputEditText) view.findViewById(R.id.nameTxt);
        txt_mobile = (TextInputEditText) view.findViewById(R.id.mobileTxt);
        txt_add1 = (TextInputEditText) view.findViewById(R.id.add1Txt);
        txt_add2 = (TextInputEditText) view.findViewById(R.id.add2Txt);
        txt_street_locality = (TextInputEditText) view.findViewById(R.id.street_localityTxt);
        txt_landmarkTxt = (TextInputEditText) view.findViewById(R.id.landmarkTxt);
        txt_pinTxt = (TextInputEditText) view.findViewById(R.id.pinTxt);
        txt_district = (TextInputEditText) view.findViewById(R.id.districtTxt);
        isDeliverable=(Button)view.findViewById(R.id.btn);
        btnCheckout=(Button)view.findViewById(R.id.button_buy);

        Bundle bundle=getArguments();
        prdid=bundle.getString("prdidToAddress");
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

//        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//
//        }
//        if (!mLocationManager.isLocationEnabled()) {
//            displayLocationSettingsRequest(getActivity());
//        }
//            if (mLocationManager.isLocationEnabled()) {
//                Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if(location!=null)
//                {
//                    onLocationChanged(location);
//                }
//
//            }

            isDeliverable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    address="";
                    if(!TextUtils.isEmpty(txt_add1.getText()))
                        address+=txt_add1.getText();

                    if(!TextUtils.isEmpty(txt_add2.getText()))
                        address+=", "+txt_add2.getText();

                    if(!TextUtils.isEmpty(txt_street_locality.getText()))
                        address+=", "+txt_street_locality.getText();

                    if(!TextUtils.isEmpty(txt_landmarkTxt.getText()))
                        address+=", "+txt_landmarkTxt.getText();

                    if(!TextUtils.isEmpty(txt_pinTxt.getText()))
                        address+=", "+txt_pinTxt.getText();

                    if(!TextUtils.isEmpty(txt_district.getText()))
                        address+=", "+txt_district.getText();

                    Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> addresses=geocoder.getFromLocationName(address,1);
                        Double latitude=addresses.get(0).getLatitude();
                        Double longitude=addresses.get(0).getLongitude();

                        List<Address> vendor_address=geocoder.getFromLocationName("Pune Railway Station",1);
                        Double main_latitude=vendor_address.get(0).getLatitude();
                        Double main_longitude=vendor_address.get(0).getLongitude();

                        if(isDeliverableStatus(main_latitude,main_longitude,latitude,longitude))
                        {
                            Toast.makeText(getActivity(), "Delivery Available", Toast.LENGTH_SHORT).show();
                            btnCheckout.setEnabled(true);
                        }else
                        {
                            Toast.makeText(getActivity(), "Delivery Not Available", Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeDrawableActivity)getActivity()).showProductBillFragment(prdid,address,txt_name.getText().toString(),txt_mobile.getText().toString());
            }
        });




        return view;
    }

    private boolean isDeliverableStatus(Double main_latitude, Double main_longitude, Double latitude, Double longitude) {

        Location locationA = new Location("point A");
        locationA.setLatitude(main_latitude);
        locationA.setLongitude(main_longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);

        double distance = locationA.distanceTo(locationB)/1000;
        System.out.println(distance);
        if(distance<10)
        {
            return true;
        }else
        {
            return false;
        }


    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }




    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), "address : " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        Geocoder geocoder=new Geocoder(getActivity());
        List<Address>addresses;
        try {
            addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            String locality= addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
            String add1="";
            if(!TextUtils.isEmpty(locality))
            add1=address.substring(0,address.indexOf(locality)-2);
            else
                add1=address.substring(0,address.indexOf(city)-2);

            String a1="",a2="";
            txt_add1.setText(add1);

            txt_street_locality.setText(locality);
            txt_pinTxt.setText(zip);
            txt_district.setText(city);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



}


