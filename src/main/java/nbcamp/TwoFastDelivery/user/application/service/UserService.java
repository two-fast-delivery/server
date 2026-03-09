package nbcamp.TwoFastDelivery.user.application.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.user.application.dto.CreateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.UpdateUserRequest;
import nbcamp.TwoFastDelivery.user.application.dto.UpdateUserStatusRequest;
import nbcamp.TwoFastDelivery.user.application.dto.UserResponse;
import nbcamp.TwoFastDelivery.user.domain.User;
import nbcamp.TwoFastDelivery.user.domain.UserId;
import nbcamp.TwoFastDelivery.user.infrastructure.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public UserResponse createUser(CreateUserRequest request){

        //1.이메일 중복 체크
        if(userJpaRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException(("이미 존재하는 이메일 입니다."));
        }

        // 2.user생성
        User user = User.create(
                request.getUsername(),
                request.getEmail(),
                request.getNickname(),
                request.getPassword()
        );

        //3.DB 저장
        userJpaRepository.save(user);

        //4.응답
        return UserResponse.from(user);
    }

    public UserResponse getUser(UserId userId) {

        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(UserId userId, UpdateUserRequest request){

        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        user.update(request.getNickname(), request.getPassword());

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUserStatus(UserId uesrId, UpdateUserStatusRequest request){

        User user = userJpaRepository.findById(uesrId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        user.updateStatus(request.getStatus());

        return UserResponse.from(user);
    }
}
