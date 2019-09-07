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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sagar.digishopper.HomeDrawableActivity;
import com.project.sagar.digishopper.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements LocationListener {
    public static final String TAG = MapFragment.class.getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationManager mLocationManager;
    private TextInputEditText txt_name,txt_mobile,txt_add1,txt_add2, txt_street_locality, txt_landmarkTxt, txt_pinTxt, txt_district;
    private Button btnCheckout;
    String address="",prdid="",parentFrag="";
    public HashMap<String, ArrayList<String>> cartmap=new HashMap<>();
    int qty;
    DatabaseReference dbr;
    String uid;

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

        btnCheckout=(Button)view.findViewById(R.id.button_buy);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbr= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("user_fname").getValue()!=null)
                {
                    String fname=dataSnapshot.child("user_fname").getValue().toString();
                    if(dataSnapshot.child("user_lname").getValue()!=null)
                    {
                        String lname=dataSnapshot.child("user_lname").getValue().toString();
                        txt_name.setText(fname+" "+lname);
                    }
                }

                if(dataSnapshot.child("user_phone_number").getValue()!=null)
                {
                    String mobile=dataSnapshot.child("user_phone_number").getValue().toString();
                    txt_mobile.setText(mobile);
                }

                if(dataSnapshot.child("user_address").child("add1").getValue()!=null)
                {
                    txt_add1.setText(dataSnapshot.child("user_address").child("add1").getValue().toString());
                }


                if(dataSnapshot.child("user_address").child("add2").getValue()!=null)
                {
                    txt_add2.setText(dataSnapshot.child("user_address").child("add2").getValue().toString());
                }


                if(dataSnapshot.child("user_address").child("street_locality").getValue()!=null)
                {
                    txt_street_locality.setText(dataSnapshot.child("user_address").child("street_locality").getValue().toString());
                }


                if(dataSnapshot.child("user_address").child("landmark").getValue()!=null)
                {
                    txt_landmarkTxt.setText(dataSnapshot.child("user_address").child("landmark").getValue().toString());
                }


                if(dataSnapshot.child("user_address").child("district").getValue()!=null)
                {
                    txt_district.setText(dataSnapshot.child("user_address").child("district").getValue().toString());
                }


                if(dataSnapshot.child("user_address").child("pin").getValue()!=null)
                {
                    txt_pinTxt.setText(dataSnapshot.child("user_address").child("pin").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            parentFrag=bundle.getString("parentFrag");
           if(parentFrag.equals("cart"))
           {
               cartmap=(HashMap<String, ArrayList<String>>) bundle.getSerializable("cartmap");
           }
           else if(parentFrag.equals("prdhome1"))
           {
               btnCheckout.setText("Add Adddress");
           }
           else
           {
               prdid=bundle.getString("prdidToAddress");
               qty=bundle.getInt("qty");
           }
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnCheckout.getText().equals("Add Adddress"))
                {

                    if(txt_add1.getText()!=null)
                    {
                        dbr.child("user_address").child("add1").setValue(txt_add1.getText().toString());
                    }
                    if(txt_add2.getText()!=null)
                    {
                        dbr.child("user_address").child("add2").setValue(txt_add2.getText().toString());
                    }
                    if(txt_street_locality.getText()!=null)
                    {
                        dbr.child("user_address").child("street_locality").setValue(txt_street_locality.getText().toString());
                    }
                    if(txt_landmarkTxt.getText()!=null)
                    {
                        dbr.child("user_address").child("landmark").setValue(txt_landmarkTxt.getText().toString());
                    }
                    if(txt_pinTxt.getText()!=null)
                    {
                        dbr.child("user_address").child("pin").setValue(txt_pinTxt.getText().toString());
                    }
                    if(txt_district.getText()!=null)
                    {
                        dbr.child("user_address").child("district").setValue(txt_district.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "address added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else
                    {
                        txt_district.setError("District value can't be null");
                    }

                }else{
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
                            if(parentFrag.equals("cart"))
                            {
                                ((HomeDrawableActivity)getActivity()).showProductBillFragment(cartmap,address,txt_name.getText().toString(),txt_mobile.getText().toString());

                            }else
                            {
                                ArrayList<String>prdlist=new ArrayList<>();
                                ArrayList<String>qtylist=new ArrayList<>();
                                prdlist.add(prdid);
                                qtylist.add(String.valueOf(qty));
                                HashMap<String,ArrayList<String>> map=new HashMap<String, ArrayList<String>>();
                                map.put("prd",prdlist);
                                map.put("qty",qtylist);
                                ((HomeDrawableActivity)getActivity()).showProductBillFragment(map,address,txt_name.getText().toString(),txt_mobile.getText().toString());
                            }
                        }else
                        {
                            Toast.makeText(getActivity(), "Delivery Not Available", Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }










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


