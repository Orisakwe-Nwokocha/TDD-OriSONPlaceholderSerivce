package dev.orisha.orison.post.data.model;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Embedded;

@Data
public class Address {

    private String street;
    private String suite;
    private String city;
    private String zipcode;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY, prefix = "geo_")
    private Geo geo;

}
