package com.gamarraloop.platform.profiles.application.ports.output;

import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.valueobjects.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    List<UserProfile> findByRole(UserRole role);
}
