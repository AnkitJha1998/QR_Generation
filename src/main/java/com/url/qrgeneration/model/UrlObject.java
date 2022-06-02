package com.url.qrgeneration.model;

import javax.persistence.*;

@Entity
public class UrlObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "url_object_gen")
    @SequenceGenerator(name = "url_object_gen", sequenceName = "url_object_gen", initialValue = 1, allocationSize = 1)
    private Long urlId;

    private String url;
    private String urlShortId;

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlShortId() {
        return urlShortId;
    }

    public void setUrlShortId(String urlShortId) {
        this.urlShortId = urlShortId;
    }
}
