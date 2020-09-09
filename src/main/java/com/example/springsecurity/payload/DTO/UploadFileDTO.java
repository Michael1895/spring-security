package com.example.springsecurity.payload.DTO;

public class UploadFileDTO {

    private String resource_name;
    private String fileDownloadUri;
    private String content_type;
    private long size;

    public UploadFileDTO(String resource_name, String fileDownloadUri, String content_type, long size) {
        this.resource_name = resource_name;
        this.fileDownloadUri = fileDownloadUri;
        this.content_type = content_type;
        this.size = size;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}