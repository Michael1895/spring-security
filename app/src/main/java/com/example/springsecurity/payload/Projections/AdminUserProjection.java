package com.example.springsecurity.payload.Projections;

import com.example.springsecurity.models.Resources;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.payload.DTO.ResourcesDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AdminUserProjection extends UserProjection {
    Set<Role> getRoles();
    String getFullName();
    UUID getOauthId();
    String getToken();
    LocalDateTime getTokenCreationDate();
    Boolean getIsEnabled();
    List<Resources> getResources();
}
