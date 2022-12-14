package com.example.weathertest;

import static com.example.weathertest.Utiles.CastLatXLongY.TO_GRID;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.weathertest.Utiles.CastLatXLongY;
import com.example.weathertest.data.Clothes;
import com.example.weathertest.databinding.ActivityMainBinding;
import com.example.weathertest.manager.ClothesManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private double lat = 37.374990564681;
    private double lon = 126.92943198848;
    private Calendar cal = Calendar.getInstance();
    private String API_KEY = "2e58C7aUEoFp1ToHIEta1FRXX+5d6ylUGgIF8Jcmakmby5VG7oyAK1CZHrdd2/sVCS1R4U2g+RO+IDnDmP8JJA==";
    private Date nowDate = new Date();
    private String temp = "";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    private String base_date = simpleDateFormat.format(nowDate);
    private String base_time = new SimpleDateFormat("HH00").format(nowDate);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initView();
    }
    // View
    private void initView() {
        String addressItem = getAddress(getApplicationContext(), lat, lon);
        loadWeather(lat, lon);
        binding.ivWeatherIcon.setOnClickListener(MainActivity.this);
        binding.btSearch.setOnClickListener(MainActivity.this);
        binding.tvWeatherClick.setText(addressItem);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivWeatherIcon:
                break;
            case R.id.btSearch:
                List<Address> list = getReverseAddress(MainActivity.this, binding.etAddress.getText().toString());
                double changeLat = list.get(0).getLatitude();
                double changeLon = list.get(0).getLongitude();
                String addressChange = getAddress(getApplicationContext(), changeLat, changeLon);
                binding.tvWeatherClick.setText(addressChange);
                loadWeather(changeLat, changeLon);
                binding.etAddress.setText("");
                break;
        }
    }

    // ?????? ???????????? -  ??????????????? ?????? ??????
    private String timeCheck()  {
        int intTime = Integer.parseInt(base_time); // 0200
        if (intTime < 200) {
            Date setDate = null;
            try {
                setDate = simpleDateFormat.parse(base_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(setDate);
            cal.add(Calendar.DATE, -1);
            base_time = simpleDateFormat.format("2300");
        } else if (intTime < 500) {
            base_time = "0200";
        } else if (intTime < 800) {
            base_time = "0500";
        } else if (intTime < 1100) {
            base_time = "0800";
        } else if (intTime < 1400) {
            base_time = "1100";
        } else if (intTime < 1700) {
            base_time = "1400";
        } else if (intTime < 2000) {
            base_time = "1700";
        } else if (intTime < 2300) {
            base_time = "2000";
        } else {
            base_time = "2300";
        }
        return base_time;
    }

    private void loadWeather(double lat, double lon) {
        timeCheck();
        // ????????? ??????
        CastLatXLongY castLatXLongY = new CastLatXLongY(TO_GRID, lat, lon);
        int nx = (int) castLatXLongY.getNx();
        int ny = (int) castLatXLongY.getNy();
        API.getRetrofit().create(WeatherAPI.class).getCurrentWeather(API_KEY, base_date, base_time, nx, ny).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    setWeatherData(weatherResponse.getResponse());
                } else {
                    int code = response.code();
                    onFailure(call, new Exception("onResponse fail, code: " + code));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // ???????????? ?????? set
    private void setWeatherData(WeatherResponse.Response weatherResponse) {
        temp = "";
        for (WeatherResponse.Items.Item item : weatherResponse.body.getItems().getItem()) {
            String value = getValueOfCategory(item);
            if (temp.isEmpty()) {
                temp = value;
            } else {
                temp += (value);
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tvWeather.setText(temp);
            }
        });
    }

    // api ???????????? ??????
    private String getValueOfCategory(WeatherResponse.Items.Item item) {
        String changeValue = null;
        switch (item.getCategory()) {
            // ?????? ??????
            case "TMP":
                changeValue = "?????? ?????? : " + item.getFcstValue() + "???   ";
                int intTMP = Integer.parseInt(item.getFcstValue());// ?????? ??????
                Clothes clothes = ClothesManager.checkWeatherData(intTMP);

                clothesRecommendation(clothes.getClothesUrl());
                binding.tvList.setText("?????? ?????? : " + clothes.getClothesComment());
                break;
            case "UUU": //??????(????????????) m/s
            case "VVV": //??????(????????????) m/s
            case "VEC": //?????? deg
            case "WAV": //????????????
                changeValue = "";
                break;
            // ?????? m/s
            case "WSD":
                double doubleWSD = Double.parseDouble(item.getFcstValue());// ?????? ??????
                changeValue = "?????? : " + ClothesManager.getWindSpeedDesction(doubleWSD);
                break;
            //????????????
            case "SKY":
                switch (item.getFcstValue()) {
                    case "1":
                        changeValue = "???????????? : ?????? ";
                        break;
                    case "3":
                        changeValue = "???????????? : ???????????? ";
                        break;
                    case "4":
                        changeValue = "???????????? : ?????? ";
                        break;
                }
                break;
            // ????????????
            case "PTY":
                switch (item.getFcstValue()) {
                    //??????(0), ???(1), ???/???(2), ???(3), ?????????(4)
                    case "0":
                        changeValue = "";
                        break;
                    case "1":
                        changeValue = "??? ";
                        break;
                    case "2":
                        changeValue = "???/??? ";
                        break;
                    case "3":
                        changeValue = "??? ";
                        break;
                    case "4":
                        changeValue = "????????? ";
                        break;
                }
                break;

            //?????? ??????
            case "POP":
                changeValue = "???????????? : " + item.getFcstValue() + "%";
                if (!item.getFcstValue().equals("0")) {
                    binding.tvUmbrella.setText("?????? ???????????? !!");
                    binding.ivUmbrella.setImageResource(R.drawable.ic_umbrella);
                } else {
                    binding.tvUmbrella.setText("?????? ????????? ???????????? ?????????");
                    binding.tvUmbrella.setTextColor(Color.WHITE);
                    binding.ivUmbrella.setImageResource(R.drawable.ic_sun);
                }
                break;

            // ?????????
            case "PCP":
                if (item.getFcstValue().equals("1")) {
                    changeValue = "????????? ????????? : " + item.getFcstValue() + "?????? (1 mm)\n";
                } else {
                    changeValue = "";
                }
                break;
        }
        return changeValue;
    }

    //?????? ????????? ????????????
    private String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "?????? ????????? ?????? ??? ??? ????????????.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString(); // ?????? ????????????
                    nowAddress = currentLocationAddress;
                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "????????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return nowAddress;
    }

    //????????? ?????? ?????? ?????? ( getLocationFromAddress )
    private List<Address> getReverseAddress(Context mContext, String location) {
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            Toast.makeText(mContext, "????????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return address;
    }

    // ????????? ?????? ?????? ??????
    private void clothesRecommendation(String imaUrl) {
        Glide.with(MainActivity.this)
                .load(imaUrl)
                .into(binding.ivWeatherIcon);
    }

    // motion event
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
