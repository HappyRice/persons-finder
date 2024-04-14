package com.persons.finder.data.persistence;

import com.persons.finder.data.model.Location;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository extends BaseRepository {

    public void saveLocation(final Location location) {
        this.persist(location);
    }

    public void removeLocation(final Location location) {
        this.delete(location);
    }

}