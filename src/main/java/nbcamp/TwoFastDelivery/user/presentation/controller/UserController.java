package nbcamp.TwoFastDelivery.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.user.application.dto.*;
import nbcamp.TwoFastDelivery.user.application.service.UserRoleChangeService;
import nbcamp.TwoFastDelivery.user.application.service.UserService;
import nbcamp.TwoFastDelivery.user.domain.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRoleChangeService userRoleChangeService;

    @PostMapping
    public UserResponse createUser(@RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        return userService.getUser(UserId.of(userId));
    }

    @PatchMapping("/{userId}")
    public UserResponse updateUser(@PathVariable UUID userId,
                                   @RequestBody UpdateUserRequest request){
        return userService.updateUser(UserId.of(userId), request);
    }

    @PatchMapping("/{userId}/status")
    public UserResponse updateUserStatus(@PathVariable UUID userId,
                                         @RequestBody UpdateUserStatusRequest request){
        return userService.updateUserStatus(UserId.of(userId), request);
    }

    @PostMapping("{userId}/role-change-requests")
    public ResponseEntity<RoleChangeRequestResponse> createRoleChangeRequest(
            @PathVariable UUID userId,
            @RequestBody RoleChangeRequestCreateRequest request
    ){
        RoleChangeRequestResponse response = userRoleChangeService.createRoleChangeRequest(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/role-change-requests/{requestId}/approve")
    public ResponseEntity<Void> approveRoleChangeRequest(
            @PathVariable UUID requestId,
            @RequestParam UUID adminId
    ){
        userRoleChangeService.approveRoleChangeRequest(requestId, adminId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/role-change-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectRoleChangeRequest(
            @PathVariable UUID requestId,
            @RequestParam UUID adminId
    ){
        userRoleChangeService.rejectRoleChangeRequest(requestId, adminId);
        return ResponseEntity.ok().build();
    }
}
