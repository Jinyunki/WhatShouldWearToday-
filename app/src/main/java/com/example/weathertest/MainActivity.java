package com.example.weathertest;

import static com.example.weathertest.Utiles.CastLatXLongY.TO_GRID;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
                List<Address> list = getReverseAddress2(MainActivity.this, binding.etAddress.getText().toString());
                double changeLat = list.get(0).getLatitude();
                double changeLon = list.get(0).getLongitude();
                String addressChange = getAddress(getApplicationContext(), changeLat, changeLon);
                binding.tvWeatherClick.setText(addressChange);
                loadWeather(changeLat, changeLon);
                binding.etAddress.setText("");
                break;
        }
    }

    // 단기 예보조회 -  제공시간에 맞춰 변환
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
        // 격자형 변환
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

    // 카테코리 정보 set
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

    // api 카테코리 정렬
    private String getValueOfCategory(WeatherResponse.Items.Item item) {
        String changeValue = null;
        switch (item.getCategory()) {
            // 현재 기온
            case "TMP":
                changeValue = "현재 기온 : " + item.getFcstValue() + "℃   ";
                int intTMP = Integer.parseInt(item.getFcstValue());// 현재 온도
                Clothes clothes = ClothesManager.checkWeatherData(intTMP);

                clothesRecommendation(clothes.getClothesUrl());
                binding.tvList.setText("추천 의상 : " + clothes.getClothesComment());
                break;
            case "UUU": //풍속(동서성분) m/s
            case "VVV": //풍속(남북성분) m/s
            case "VEC": //풍향 deg
            case "WAV": //파도높이
                changeValue = "";
                break;
            // 풍속 m/s
            case "WSD":
                double doubleWSD = Double.parseDouble(item.getFcstValue());// 현재 풍속
                changeValue = "풍속 : " + ClothesManager.getWindSpeedDesction(doubleWSD);
                break;
            //하늘상태
            case "SKY":
                switch (item.getFcstValue()) {
                    case "1":
                        changeValue = "하늘상태 : 맑음 ";
                        break;
                    case "3":
                        changeValue = "하늘상태 : 구름많음 ";
                        break;
                    case "4":
                        changeValue = "하늘상태 : 흐림 ";
                        break;
                }
                break;
            // 기상상태
            case "PTY":
                switch (item.getFcstValue()) {
                    //없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
                    case "0":
                        changeValue = "";
                        break;
                    case "1":
                        changeValue = "비 ";
                        break;
                    case "2":
                        changeValue = "비/눈 ";
                        break;
                    case "3":
                        changeValue = "눈 ";
                        break;
                    case "4":
                        changeValue = "소나기 ";
                        break;
                }
                break;

            //강수 확률
            case "POP":
                changeValue = "강수확률 : " + item.getFcstValue() + "%";
                if (!item.getFcstValue().equals("0")) {
                    binding.tvUmbrella.setText("우산 챙기세요 !!");
                    binding.ivUmbrella.setImageResource(R.drawable.ic_umbrella);
                } else {
                    binding.tvUmbrella.setText("우산 챙기지 않으셔도 됩니다");
                    binding.tvUmbrella.setTextColor(Color.WHITE);
                    binding.ivUmbrella.setImageResource(R.drawable.ic_sun);
                }
                break;

            // 강수량
            case "PCP":
                if (item.getFcstValue().equals("1")) {
                    changeValue = "시간당 강수량 : " + item.getFcstValue() + "범주 (1 mm)\n";
                } else {
                    changeValue = "";
                }
                break;
        }
        return changeValue;
    }

    //위도 경도로 주소찾기
    private String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString(); // 전체 상세주소
                    nowAddress = currentLocationAddress;
                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return nowAddress;
    }

    //주소로 위도 경도 찾기 ( getLocationFromAddress )
    private List<Address> getReverseAddress2(Context mContext, String location) {
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            Toast.makeText(mContext, "좌표를 가져 올 수 없습니다.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        return address;
    }

    // 온도에 따른 의류 추천
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
