package com.gamarraloop.platform.profiles.application.internal.commandservices;

import com.gamarraloop.platform.profiles.application.ports.output.UserProfileRepository;
import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.valueobjects.UserRole;
import com.gamarraloop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileCommandServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileCommandServiceImpl userProfileCommandService;

    private UserProfile createUserProfile() {

        CreateUserProfileCommand command =
                new CreateUserProfileCommand(
                        "Elizabeth Huanaco",
                        "elizabeth@gmail.com",
                        "999888777",
                        "ARTESANO",
                        "device-123"
                );

        return new UserProfile(command);
    }

    @Test
    void shouldCreateUserProfileSuccessfully() {

        CreateUserProfileCommand command =
                new CreateUserProfileCommand(
                        "Elizabeth Huanaco",
                        "elizabeth@gmail.com",
                        "999888777",
                        "ARTESANO",
                        "device-123"
                );

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile result =
                userProfileCommandService.create(command);

        ArgumentCaptor<UserProfile> captor =
                ArgumentCaptor.forClass(UserProfile.class);

        verify(userProfileRepository)
                .save(captor.capture());

        UserProfile savedProfile =
                captor.getValue();

        assertEquals(
                "Elizabeth Huanaco",
                savedProfile.getFullName()
        );

        assertEquals(
                "elizabeth@gmail.com",
                savedProfile.getEmail()
        );

        assertEquals(
                "999888777",
                savedProfile.getPhone()
        );

        assertEquals(
                UserRole.ARTESANO,
                savedProfile.getRole()
        );

        assertNotNull(result);
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() {

        UUID profileId =
                UUID.randomUUID();

        UserProfile profile =
                createUserProfile();

        UpdateUserProfileCommand command =
                new UpdateUserProfileCommand(
                        "Elizabeth Updated",
                        "updated@gmail.com",
                        "987654321"
                );

        when(userProfileRepository.findById(profileId))
                .thenReturn(Optional.of(profile));

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfile result =
                userProfileCommandService.update(
                        profileId,
                        command
                );

        assertEquals(
                "Elizabeth Updated",
                result.getFullName()
        );

        assertEquals(
                "updated@gmail.com",
                result.getEmail()
        );

        assertEquals(
                "987654321",
                result.getPhone()
        );

        verify(userProfileRepository)
                .findById(profileId);

        verify(userProfileRepository)
                .save(profile);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUserProfile() {

        UUID profileId =
                UUID.randomUUID();

        UpdateUserProfileCommand command =
                new UpdateUserProfileCommand(
                        "Updated",
                        "updated@gmail.com",
                        "987654321"
                );

        when(userProfileRepository.findById(profileId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileCommandService.update(
                        profileId,
                        command
                )
        );

        verify(userProfileRepository, never())
                .save(any());
    }

}