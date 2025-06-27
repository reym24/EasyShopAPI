package org.yearup.data;


import org.yearup.models.Product;
import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile getByUserId(int profileId);
    Profile create(Profile profile);
    void update(int profileId, Profile profile);
}