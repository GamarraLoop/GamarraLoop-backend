package com.gamarraloop.platform.profiles.application.internal.queryservices;

import com.gamarraloop.platform.profiles.application.ports.input.UserProfileQueryService;
import com.gamarraloop.platform.profiles.application.ports.output.UserProfileRepository;
import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.valueobjects.UserRole;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserProfileQueryServiceImpl implements UserProfileQueryService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileQueryServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile getById(UUID id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", id));
    }

    @Override
    public List<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }

    @Override
    public List<UserProfile> getByRole(String role) {
        var userRole = UserRole.valueOf(role.toUpperCase());
        return userProfileRepository.findByRole(userRole);
    }
}
