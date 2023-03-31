package com.sdapps.formula1fy.core.utils;

public class StringHelper {

    public StringHelper() {
    }

    public String getQueryFormat(final String data) {
        if (data != null) {
            return "'" + data + "'";
        } else
            return null;
    }

    public String removeWhiteSpaces(final String data){
        if(data !=null){
            return data.replaceAll("\\s", "");
        }else
            return null;
    }
}
