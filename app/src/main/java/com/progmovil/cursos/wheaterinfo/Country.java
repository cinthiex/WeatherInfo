package com.progmovil.cursos.wheaterinfo;

import java.util.Date;

/**
 * Created by cinthia.martinez on 22/09/2015.
 */

public class Country {
    private String countryName;
    private String temp;
    private String temp_min;
    private String temp_max;
    private String humidity;
    private String description;
    private String icon;

    public Country() {
    }

    public Country(String countryName, String temp, String temp_min,String temp_max, String humidity, String description,String icon) {
        this.countryName = countryName;
        this.temp = temp;
        this.temp_min=temp_min;
        this.temp_max= temp_max;
        this.humidity= humidity;
        this.description=description;
        this.icon=icon;


    }

    public String getCountryName() {
        return countryName;
    }

    public String getTemp() {      return temp;      }

    public String getTempMin() {      return temp_min;      }
    public String getTempMax() {      return temp_max;      }
    public String getHumidity() {      return humidity;      }
    public String getDescription() {      return description;      }
    public String getIcon() {      return icon;      }



}