package edu.iot.locker.business;

import edu.iot.locker.data.model.LockerEntity;
import edu.iot.locker.data.model.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Data
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authorizer {
    HttpServletResponse response;
    LockerEntity locker;
    String lockerId;
    UserEntity user;
    String token;
    String method;

    public boolean checkAuthorization(boolean checkUndefinedLocker, boolean checkUndefinedUser,
                                      boolean checkLockerFree, boolean checkAdmin) {
        if (checkUndefinedLocker) {
            if (locker == null) {
                log.error("LCKR {} {}\t{}", method, token, "NOT_FOUND");
                sendError(response, HttpServletResponse.SC_NOT_FOUND);
                return true;
            }
        }

        if (checkUndefinedUser) {
            if (user == null) {
                log.error("USER {} {}\t{}", method, token, "UNAUTHORIZED");
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
                return true;
            }
        }

        if (checkLockerFree) {
            if (locker.getOwner() != null) {
                log.error("LCKR {} {}\t{}\t{}", method, lockerId, "ALREADY_TAKEN", token);
                sendError(response, HttpServletResponse.SC_FORBIDDEN);
                return true;
            }
        }

        if (checkAdmin) {
            if (!token.equals("adminToken")) {
                log.error("LCKR {} {}\t{}", method, lockerId, "UNAUTHORIZED");
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
                return true;
            }
        }

        return false;
    }

    private void sendError(HttpServletResponse response, int error) {
        try {
            response.sendError(error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
