package com.example.weathertest.Utiles;

public class CastLatXLongY {

    private int type;

    private double lat;
    private double lng;

    private double nx;
    private double ny;

    public double getNx() {
        return nx;
    }

    public double getNy() {
        return ny;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setNx(double nx) {
        this.nx = nx;
    }

    public void setNy(double ny) {
        this.ny = ny;
    }

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    public CastLatXLongY(int type, double latX, double lonY) {
        this.type = type;
        this.lat = latX;
        this.lng = lonY;

        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        if (type == TO_GRID) {
            double ra = Math.tan(Math.PI * 0.25 + (latX) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lonY * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            nx = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            ny = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        } else {
            nx = latX;
            ny = lonY;
            double xn = latX - XO;
            double yn = ro - lonY + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                } else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            lat = alat * RADDEG;
            lng = alon * RADDEG;
        }
    }
}
