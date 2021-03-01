package edu.iot.locker.controller;

import edu.iot.locker.business.Authorizer;
import edu.iot.locker.config.LockerConfig;
import edu.iot.locker.data.metadata.LockerAction;
import edu.iot.locker.data.metadata.LockerStatus;
import edu.iot.locker.data.model.LockerEntity;
import edu.iot.locker.data.model.UserEntity;
import edu.iot.locker.model.metadata.LockerInfo;
import edu.iot.locker.model.metadata.LocketHistoryInfo;
import edu.iot.locker.model.response.LockerInfoResponse;
import edu.iot.locker.model.response.LockersInfoResponse;
import edu.iot.locker.model.response.StatusResponse;
import edu.iot.locker.service.HistoryService;
import edu.iot.locker.service.LockerService;
import edu.iot.locker.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/locker")
public class LockerController {

    private final UserService userService;
    private final LockerService lockerService;
    private final HistoryService historyService;
    private final LockerConfig lockerConfig;

    @GetMapping(value = "/status")
    private StatusResponse getStatus(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        UserEntity user = userService.findByToken(token);

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, null, null, user, token, "STAT");
            if (authorizer.checkAuthorization(false, true, false, false)) {
                return null;
            }
        }

        StatusResponse result;
        if (user.getLocker() == null) {
            result = new StatusResponse(("NoLockerFound"));
            log.warn("LCKR STAT {}\t{}", token, result.getStatus());
        } else {
            result = new StatusResponse(user.getLocker().getStatus().toString());
            log.info("LCKR STAT {}\t{}\t{}", user.getUsername(), user.getLocker().getId(), result.getStatus());
        }
        return result;
    }

    @GetMapping(value = "/{id}/status")
    private StatusResponse getStatusId(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("id") String lockerId
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        LockerEntity locker = lockerService.findById(Integer.parseInt(lockerId));

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, locker, lockerId, null, token, "STAT");
            if (authorizer.checkAuthorization(true, false, false, true)) {
                return null;
            }
        }

        StatusResponse result = new StatusResponse(locker.getStatus().toString());
        log.info("LCKR STAT {}\t{}", lockerId, result.getStatus());
        return result;
    }

    @PutMapping(value = "/open")
    private boolean openLocker(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        UserEntity user = userService.findByToken(token);

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, user.getLocker(), null, user, token, "OPEN");
            if (authorizer.checkAuthorization(true, true, false, false)) {
                return false;
            }
        }

        LockerEntity locker = user.getLocker();
        locker.setStatus(LockerStatus.OPEN);
        lockerService.update(locker);
        log.info("LCKR OPEN {}", locker.getId());

        historyService.addHistory(locker, LockerAction.OPEN);

        return true;
    }

    @PutMapping(value = "/{id}/open")
    private boolean openLockerId(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("id") String lockerId
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        LockerEntity locker = lockerService.findById(Integer.parseInt(lockerId));

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, locker, lockerId, null, token, "OPEN");
            if (authorizer.checkAuthorization(true, false, false, true)) {
                return false;
            }
        }

        locker.setStatus(LockerStatus.OPEN);
        lockerService.update(locker);
        log.info("LCKR OPEN {}", lockerId);

        historyService.addHistory(locker, LockerAction.OPEN);

        return true;
    }

    @PostMapping(value = "/take")
    private int takeLocker(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        UserEntity user = userService.findByToken(token);

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, null, null, user, token, "TAKE");
            if (authorizer.checkAuthorization(false, true, false, false)) {
                return -1;
            }
        }

        LockerEntity locker = lockerService.findFreeLocker();
        if (locker != null) {
            locker.setOwner(user);
            lockerService.update(locker);
            log.info("LCKR TAKE {}\t{}", locker.getId(), user.getId());
            historyService.addHistory(locker, LockerAction.TAKE);
            return locker.getId();
        } else {
            log.info("LCKR TAKE {}\t{}", "NO Locker Free!", user.getId());
            return -1;
        }
    }

    @PutMapping(value = "/return")
    private boolean returnLocker(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //TODO: implement security layer
        String token = request.getHeader("token");
        UserEntity user = userService.findByToken(token);
        LockerEntity locker = user.getLocker();

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, locker, null, user, token, "RTRN");
            if (authorizer.checkAuthorization(true, true,
                    false, false)) {
                return false;
            }
        }

        locker.setOwner(null);
        lockerService.update(locker);
        log.info("LCKR RTRN {}\t{}", user.getUsername(), locker.getId());

        historyService.addHistory(locker, LockerAction.RETURN);

        return true;
    }

    @GetMapping(value = "/{id}/info")
    private LockerInfoResponse getInfo(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("id") String lockerId
    ) {
        String token = request.getHeader("token");
        LockerEntity locker = lockerService.findById(Integer.parseInt(lockerId));

        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, locker, lockerId, null, token, "INFO");
            if (authorizer.checkAuthorization(true, false, false, true)) {
                return null;
            }
        }

        List<LocketHistoryInfo> lockerHistory = historyService.getLockerHistory(locker);
        LockerInfoResponse.LockerInfoResponseBuilder builder = LockerInfoResponse.builder()
                .lockerId(locker.getId())
                .lockerStatus(locker.getStatus())
                .currentWeight(locker.getCurrentWeight())
                .maxEndurance(locker.getMaxEndurance())
                .lastModified(locker.getLastModified())
                .locketHistoryInfos(lockerHistory);

        if (locker.getOwner() != null) {
            builder.currentOwner(userService.toInfo(locker.getOwner()));
        }

        return builder.build();
    }

    @GetMapping(value = "/info")
    private LockersInfoResponse getInfo(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String token = request.getHeader("token");
        if (lockerConfig.isAuthorizationEnable()) {
            Authorizer authorizer = new Authorizer(response, null, null, null, token, "INFO");
            if (authorizer.checkAuthorization(false, false, false, true)) {
                return null;
            }
        }

        List<LockerEntity> lockerEntities = lockerService.findAll();
        List<LockerInfo> lockersInfo = lockerEntities.parallelStream().map(lockerService::toLockerInfo).collect(Collectors.toList());

        return LockersInfoResponse.builder().lockersInfo(lockersInfo).build();
    }

    public boolean closeLocker(String lockerId) {
        LockerEntity locker = lockerService.findById(Integer.parseInt(lockerId));
        if (locker == null) {
            log.error("Close locker failed! (No such locker with id {})", lockerId);
            return false;
        }

        locker.setStatus(LockerStatus.CLOSE);
        lockerService.update(locker);
        log.info("LCKR CLSE {}", lockerId);

        historyService.addHistory(locker, LockerAction.CLOSE);
        return true;
    }
}
