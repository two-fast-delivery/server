package nbcamp.TwoFastDelivery.user.application.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.user.application.dto.request.CreateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.request.UpdateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.request.UpdateUserStatusRequest;
import nbcamp.TwoFastDelivery.user.application.dto.response.UserResponse;
import nbcamp.TwoFastDelivery.user.domain.user.User;
import nbcamp.TwoFastDelivery.user.domain.user.UserId;
import nbcamp.TwoFastDelivery.user.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(CreateUserRequest request){

        //1.이메일 중복 체크
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException(("이미 존재하는 이메일 입니다."));
        }

        // 2.user생성
        User user = User.create(
                request.getUsername(),
                request.getEmail(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword())
        );

        //3.DB 저장
        userRepository.save(user);

        //4.응답
        return UserResponse.from(user);
    }

    public UserResponse getUser(UserId userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(UserId userId, UpdateUserRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        user.update(request.getNickname(), request.getPassword());

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUserStatus(UserId userId, UpdateUserStatusRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        user.updateStatus(request.getStatus());

        return UserResponse.from(user);
    }
}