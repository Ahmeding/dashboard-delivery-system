package com.hitecart.deliverysystem.services;

import com.flickr4java.flickr.FlickrException;

import java.io.InputStream;

public interface FlickrService {

    String savePhoto(InputStream photo,String title) throws FlickrException;
}
