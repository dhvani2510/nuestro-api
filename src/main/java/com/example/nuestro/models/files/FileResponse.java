package com.example.nuestro.models.files;

import com.example.nuestro.entities.File;
import com.example.nuestro.shared.helpers.StringHelper;

public class FileResponse
{
    private String id;
    private  String name;
    private  String url;

    public FileResponse(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
    public  FileResponse(File file){
        id= file.getId();
        name=file.getName();
        url= StringHelper.GetFileUrl(file.getId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
