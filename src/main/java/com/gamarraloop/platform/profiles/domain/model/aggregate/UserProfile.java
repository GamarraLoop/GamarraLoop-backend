package com.gamarraloop.platform.profiles.domain.model.aggregate;

import com.gamarraloop.platform.profiles.domain.model.commands.CreateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;
import com.gamarraloop.platform.profiles.domain.model.valueobjects.UserRole;
import com.gamarraloop.platform.shared.domain.model.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile extends AuditableEntity {

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "device_id")
    private String deviceId;

    protected UserProfile() {
    }

    public UserProfile(CreateUserProfileCommand command) {
        this.fullName = command.fullName();
        this.email = command.email();
        this.phone = command.phone();
        this.role = UserRole.valueOf(command.role().toUpperCase());
        this.deviceId = command.deviceId();
    }

    public void updateProfile(UpdateUserProfileCommand command) {
        this.fullName = command.fullName();
        this.email = command.email();
        this.phone = command.phone();
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public UserRole getRole() {
        return role;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
