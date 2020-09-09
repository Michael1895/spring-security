package com.example.springsecurity.payload.Projections;

import com.example.springsecurity.models.Resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserProjection {

        UUID getId();
        String getUsername();
        LocalDate getBirthdate();
        String getEmail();
        UUID getAvatarId();

}
