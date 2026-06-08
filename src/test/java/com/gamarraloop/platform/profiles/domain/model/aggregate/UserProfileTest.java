package com.gamarraloop.platform.profiles.domain.model.aggregate;

import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.valueobjects.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {
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

        UserProfile profile =
                new UserProfile(command);

        assertEquals(
                "Elizabeth Huanaco",
                profile.getFullName()
        );

        assertEquals(
                "elizabeth@gmail.com",
                profile.getEmail()
        );

        assertEquals(
                "999888777",
                profile.getPhone()
        );

        assertEquals(
                UserRole.ARTESANO,
                profile.getRole()
        );

        assertEquals(
                "device-123",
                profile.getDeviceId()
        );
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() {

        UserProfile profile =
                createUserProfile();

        UpdateUserProfileCommand command =
                new UpdateUserProfileCommand(
                        "Elizabeth Updated",
                        "updated@gmail.com",
                        "987654321"
                );

        profile.updateProfile(command);

        assertEquals(
                "Elizabeth Updated",
                profile.getFullName()
        );

        assertEquals(
                "updated@gmail.com",
                profile.getEmail()
        );

        assertEquals(
                "987654321",
                profile.getPhone()
        );
    }

    @Test
    void shouldNotChangeRoleWhenUpdatingProfile() {

        UserProfile profile =
                createUserProfile();

        UserRole originalRole =
                profile.getRole();

        UpdateUserProfileCommand command =
                new UpdateUserProfileCommand(
                        "Updated",
                        "updated@gmail.com",
                        "987654321"
                );

        profile.updateProfile(command);

        assertEquals(
                originalRole,
                profile.getRole()
        );
    }

    @Test
    void shouldNotChangeDeviceIdWhenUpdatingProfile() {

        UserProfile profile =
                createUserProfile();

        String originalDeviceId =
                profile.getDeviceId();

        UpdateUserProfileCommand command =
                new UpdateUserProfileCommand(
                        "Updated",
                        "updated@gmail.com",
                        "987654321"
                );

        profile.updateProfile(command);

        assertEquals(
                originalDeviceId,
                profile.getDeviceId()
        );
    }

    @Test
    void shouldConvertRoleToUpperCaseEnum() {

        CreateUserProfileCommand command =
                new CreateUserProfileCommand(
                        "Elizabeth",
                        "elizabeth@gmail.com",
                        "999888777",
                        "artesano",
                        "device-123"
                );

        UserProfile profile =
                new UserProfile(command);

        assertEquals(
                UserRole.ARTESANO,
                profile.getRole()
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsInvalid() {

        CreateUserProfileCommand command =
                new CreateUserProfileCommand(
                        "Elizabeth",
                        "elizabeth@gmail.com",
                        "999888777",
                        "INVALID_ROLE",
                        "device-123"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new UserProfile(command)
        );
    }
}