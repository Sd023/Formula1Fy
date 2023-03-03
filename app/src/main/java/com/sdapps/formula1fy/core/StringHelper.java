package com.sdapps.formula1fy.core;

public class StringHelper {

    public StringHelper() {
    }

    public String getQueryFormat(final String data){
        if(data !=null){
            return "'" + data + "'";
        }else
            return  null;
    }
}
