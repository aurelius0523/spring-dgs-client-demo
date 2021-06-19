package com.aurelius.springdgsclientdemo.models;

import com.aurelius.connectors.dgs.graphiqlonline.generated.types.Album;

import java.util.List;

public class AlbumAlias {
    private List<Album> data;

    public List<Album> getData() {
        return data;
    }

    public AlbumAlias setData(List<Album> data) {
        this.data = data;
        return this;
    }
}
