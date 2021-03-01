package edu.iot.locker.controller;

import edu.iot.locker.business.Authorizer;
import edu.iot.locker.config.LockerConfig;
import edu.iot.locker.data.model.UserEntity;
import edu.iot.locker.model.metadata.UserHistoryInfo;
import edu.iot.locker.model.request.LoginRequest;
import edu.iot.locker.model.request.SignUpRequest;
import edu.iot.locker.model.response.LoginResponse;
import edu.iot.locker.model.response.SignUpResponse;
import edu.iot.locker.model.response.UserInfoResponse;
import edu.iot.locker.service.HistoryService;
import edu.iot.locker.service.LockerService;
import edu.iot.locker.service.UserService;
import edu.iot.locker.utils.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@RestController()
@RequestMapping("/user")
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    LockerService lockerService;
    HistoryService historyService;
    LockerConfig lockerConfig;

    @GetMapping(value = "/{id}/info")
    private UserInfoResponse getInfo(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("id") String userId
    ) {
        String token = request.getHeader("token");
        UserEntity user = userService.findById(Integer.parseInt(userId));

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, null, null, user, token, "INFO");
            if (authorizer.checkAuthorization(false, true,
                    false, true)) {
                return null;
            }
        }

        List<UserHistoryInfo> userHistory = historyService.getUserHistory(user);
        UserInfoResponse.UserInfoResponseBuilder builder = UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickName(user.getNickName())
                .token(user.getToken())
                .signUpAt(user.getSignUpAt())
                .lastLoginAt(user.getLastLoginAt())
                .userHistoryInfos(userHistory);

        if (user.getLocker() != null) {
            builder.currentLocker(lockerService.toLockerInfo(user.getLocker()));
        }

        return builder.build();
    }

    @PostMapping(value = "/sign_up")
    private SignUpResponse getInfo(
            @RequestBody SignUpRequest signUpRequest
    ) {
        try {
            UserEntity user = new UserEntity();
            user.setNickName(signUpRequest.getNickname());
            user.setUsername(signUpRequest.getUsername());
            user.setPassword(StringUtils.getHashedString(signUpRequest.getPassword()));
            user.setSignUpAt(LocalDateTime.now());
            user.setLastLoginAt(LocalDateTime.now());

            String token = StringUtils.generateRandomString();
            user.setToken(token);

            user = userService.addUser(user);
            log.info("USER SGNP {} : {}", user.getId(), user.getUsername());

            return SignUpResponse.builder()
                    .status(SignUpResponse.SignUpStatus.OK)
                    .token(token)
                    .build();
        } catch (Exception e) {
            log.error("SignUp failed for user {}!", signUpRequest.getUsername(), e);
            return SignUpResponse.builder()
                    .status(SignUpResponse.SignUpStatus.ERROR)
                    .build();
        }
    }

    @PostMapping(value = "/login")
    private LoginResponse getInfo(
            @RequestBody LoginRequest loginRequest
    ) {
        try {
            UserEntity user = userService.findByUsername(loginRequest.getUsername());
            if (user == null) {
                log.error("No such username exist!");
                return LoginResponse.builder().status(LoginResponse.LoginStatus.NOT_EXIST).build();
            }

            String hashedPassword = StringUtils.getHashedString(loginRequest.getPassword());
            if (!hashedPassword.equals(user.getPassword())) {
                log.error("Password is wrong!");
                return LoginResponse.builder().status(LoginResponse.LoginStatus.WRONG_PASSWORD).build();
            }

            String token = StringUtils.generateRandomString();
            user.setToken(token);
            user.setLastLoginAt(LocalDateTime.now());
            userService.update(user);

            log.info("USER LGIN {}", loginRequest.getUsername());
            return LoginResponse.builder()
                    .status(LoginResponse.LoginStatus.OK)
                    .token(token)
                    .build();
        } catch (Exception e) {
            log.error("Login failed for user {}!", loginRequest.getUsername(), e);
            return LoginResponse.builder().status(LoginResponse.LoginStatus.ERROR).build();
        }
    }

}
