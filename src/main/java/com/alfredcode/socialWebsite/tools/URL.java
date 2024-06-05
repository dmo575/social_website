package com.alfredcode.socialWebsite.tools;

import org.springframework.http.RequestEntity;

import jakarta.servlet.http.HttpServletRequest;

public class URL {
    
    // returns the full URL + any added path
    public static String getUrl(HttpServletRequest req, String path) {

        String connection = req.getScheme();
        String domain = req.getServerName();
        String port = Integer.toString(req.getServerPort());

        return connection + "://" + domain + ":" + port + path;
    }

    public static String getUrl(RequestEntity<?> req, String path) {

        String connection = req.getUrl().getScheme();
        String domain = req.getUrl().getHost();
        String port = Integer.toString(req.getUrl().getPort());

        return connection + "://" + domain + ":" + port + path;
    }
}
