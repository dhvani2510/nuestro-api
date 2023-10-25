package com.example.nuestro.shared.helpers;

public class StringHelper
{
    public static boolean StringIsNullOrEmpty(String str){
        return (str == null || str.trim().isEmpty());
    }

    public  static  String GetFileUrl(String id){
      return id==null? null: "/api/v1/files/"+id ;
    }
}
