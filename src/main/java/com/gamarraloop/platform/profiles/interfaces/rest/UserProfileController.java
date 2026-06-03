package com.gamarraloop.platform.profiles.interfaces.rest;

import com.gamarraloop.platform.profiles.application.ports.input.UserProfileCommandService;
import com.gamarraloop.platform.profiles.application.ports.input.UserProfileQueryService;
import com.gamarraloop.platform.profiles.domain.model.aggregate.UserProfile;
import com.gamarraloop.platform.profiles.domain.model.commands.UpdateUserProfileCommand;
import com.gamarraloop.platform.profiles.interfaces.rest.resources.CreateUserProfileResource;
import com.gamarraloop.platform.profiles.interfaces.rest.resources.UpdateUserProfileResource;
import com.gamarraloop.platform.profiles.interfaces.rest.resources.UserProfileResource;
import com.gamarraloop.platform.profiles.interfaces.rest.transform.CreateUserProfileCommandFromResourceAssembler;
import com.gamarraloop.platform.profiles.interfaces.rest.transform.UserProfileResourceFromEntityAssembler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
public class UserProfileController {

    private final UserProfileCommandService userProfileCommandService;
    private final UserProfileQueryService userProfileQueryService;

    public UserProfileController(UserProfileCommandService userProfileCommandService,
                                 UserProfileQueryService userProfileQueryService) {
        this.userProfileCommandService = userProfileCommandService;
        this.userProfileQueryService = userProfileQueryService;
    }

    @PostMapping
    public ResponseEntity<UserProfileResource> createUserProfile(@Valid @RequestBody CreateUserProfileResource resource) {
        var command = CreateUserProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var userProfile = userProfileCommandService.create(command);
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile);
        return new ResponseEntity<>(userProfileResource, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResource> getUserProfileById(@PathVariable UUID id) {
        var userProfile = userProfileQueryService.getById(id);
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile);
        return ResponseEntity.ok(userProfileResource);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResource>> getAllUserProfiles(
            @RequestParam(value = "role", required = false) String role) {
        List<UserProfile> userProfiles;
        if (role != null && !role.isBlank()) {
            userProfiles = userProfileQueryService.getByRole(role);
        } else {
            userProfiles = userProfileQueryService.getAll();
        }
        var userProfileResources = userProfiles.stream()
                .map(UserProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userProfileResources);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResource> updateUserProfile(@PathVariable UUID id,
                                                                 @Valid @RequestBody UpdateUserProfileResource resource) {
        var command = new UpdateUserProfileCommand(resource.fullName(), resource.email(), resource.phone());
        var userProfile = userProfileCommandService.update(id, command);
        var userProfileResource = UserProfileResourceFromEntityAssembler.toResourceFromEntity(userProfile);
        return ResponseEntity.ok(userProfileResource);
    }
}
