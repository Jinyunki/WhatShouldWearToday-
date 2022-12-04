package com.example.weathertest.manager;

import com.example.weathertest.data.Clothes;


/**
 * 옷 이미지
 * 옷 설명
 * 풍속
 */
public class ClothesManager {

    /**
     * TMP Type에 경우에만 사용해야함
     * TMP Type에서 facstValue에 따른 추천 옷 데이터
     */
    public static Clothes checkWeatherData(int facstValue) {
        String clothesUrl = "";
        String clothesComment = "";

        if (facstValue <= 4) {
            clothesUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQXc1Ow0TMCp6rnGE4cqZtkG0NIy_wTuzlxhHN7KjvY0T7BHuxDtcJky3Sv-1jZuqbbAJQ&usqp=CAU";
            clothesComment = "롱패딩+목도리+바지 안에 레깅스\n멋을버리고 생명을 지키세요";
        } else if (facstValue >= 5 && facstValue <= 8) {
            clothesUrl = "https://mojikmo.com/wp-content/uploads/2022/01/%EB%82%A8%EC%9E%90-%EB%8D%94%ED%94%8C%EC%BD%94%ED%8A%B8-%EC%BD%94%EB%94%94.jpg";
            clothesComment = "코트+가디건+기모 티셔츠/바지";
        } else if (facstValue >= 9 && facstValue <= 11) {
            clothesUrl = "https://mojikmo.com/wp-content/uploads/2022/03/%EB%82%A8%EC%9E%90-%EA%B0%80%EC%A3%BD%EC%9E%90%EC%BC%93-%EC%BD%94%EB%94%94.jpg";
            clothesComment = "경량패딩/재킷+카디건+긴바지";
        } else if (facstValue >= 12 && facstValue <= 16) {
            clothesUrl = "https://s3.ap-northeast-2.amazonaws.com/storage.b-flow.io/22037994/conversions/resize.jpg";
            clothesComment = "재킷/야상+카디건+긴바지";
        } else if (facstValue >= 17 && facstValue <= 19) {
            clothesUrl = "https://usercontents-d.styleshare.io/images/47478278/640x-";
            clothesComment = "맨투맨/니트+얇은 카디건+긴바지";
        } else if (facstValue >= 20 && facstValue <= 22) {
            clothesUrl = "https://cdn.imweb.me/thumbnail/20220311/54f16e7ca2b2a.jpg";
            clothesComment = "긴소매 티+면/청바지";
        } else if (facstValue >= 23 && facstValue <= 26) {
            clothesUrl = "https://static.lookpin.co.kr/20220515230547-137f/4db83459c197ff05a74f20c4446f3e8e.jpg?resize=880";
            clothesComment = "반소매/리넨 셔츠+반바지/면바지";
        } else if (facstValue >= 27) {
            clothesUrl = "https://mojikmo.com/wp-content/uploads/2022/05/%EB%82%A8%EC%9E%90-%EB%AC%B4%EC%A7%80-%EC%98%A4%EB%B2%84%ED%95%8F-%EB%B0%98%ED%8C%94%ED%8B%B0.jpg";
            clothesComment = "반바지/반팔\n그래도 더운날씨";
        }
        return new Clothes(clothesUrl, clothesComment);
    }

    /**
     * WSD Type에서만 사용
     */
    public static String getWindSpeedDesction(double facstValue) {
        String wsdDescriont = "";
        if (facstValue <= 0.2) {
            wsdDescriont = facstValue + "m/s 0등급(고요)\n";
        } else if (facstValue >= 0.3 && facstValue <= 1.5) {
            wsdDescriont = facstValue + "m/s 1등급 (미세)\n";
        } else if (facstValue >= 1.6 && facstValue <= 3.3) {
            wsdDescriont = facstValue + "m/s 2등급 (선선)\n";
        } else if (facstValue >= 3.4 && facstValue <= 5.4) {
            wsdDescriont = facstValue + "m/s 3등급 (시원)\n";
        } else if (facstValue >= 5.4 && facstValue <= 7.9) {
            wsdDescriont = facstValue + "m/s 4등급 (약강풍)\n";
        } else if (facstValue >= 8.0 && facstValue <= 10.7) {
            wsdDescriont = facstValue + "m/s 5등급 (강풍)\n";
        } else if (facstValue >= 10.8 && facstValue <= 13.8) {
            wsdDescriont = facstValue + "m/s 6등급 (매우강풍)\n";
        } else if (facstValue >= 13.9 && facstValue <= 17.1) {
            wsdDescriont = facstValue + "m/s 7등급 (태풍)\n";
        } else if (facstValue >= 17.2 && facstValue <= 20.7) {
            wsdDescriont = facstValue + "m/s 8등급 (강한태풍)\n";
        } else {
            wsdDescriont = facstValue + "m/s 9등급 (재난)\n";
        }
        return wsdDescriont;
    }

}


