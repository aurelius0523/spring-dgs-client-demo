package com.aurelius.springdgsclientdemo.models;

public class PhotoAlbumAliasHolder {
    private AlbumAlias firstAlbum;
    private AlbumAlias secondAlbum;

    public AlbumAlias getFirstAlbum() {
        return firstAlbum;
    }

    public PhotoAlbumAliasHolder setFirstAlbum(AlbumAlias firstAlbum) {
        this.firstAlbum = firstAlbum;
        return this;
    }

    public AlbumAlias getSecondAlbum() {
        return secondAlbum;
    }

    public PhotoAlbumAliasHolder setSecondAlbum(AlbumAlias secondAlbum) {
        this.secondAlbum = secondAlbum;
        return this;
    }
}
