package com.url.qrgeneration.service;

import com.url.qrgeneration.model.UrlObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlObjectService extends JpaRepository <UrlObject, Long> {

    public Iterable<UrlObject> findByUrlShortId(String urlShortId);

}
