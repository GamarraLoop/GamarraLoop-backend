package com.gamarraloop.platform.profiles.application.internal.commandservices;

import com.gamarraloop.platform.profiles.application.ports.input.UserProfileCommandService;
import com.gamarraloop.platform.profiles.application.ports.output.UserProfileRepository;
import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserProfileCommandServiceImpl implements UserProfileCommandService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileCommandServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile create(CreateUserProfileCommand command) {
        var userProfile = new UserProfile(command);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile update(UUID id, UpdateUserProfileCommand command) {
        var userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", id));
        userProfile.updateProfile(command);
        return userProfileRepository.save(userProfile);
    }
}
