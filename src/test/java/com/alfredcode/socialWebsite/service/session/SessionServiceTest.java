package com.alfredcode.socialWebsite.service.session;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.model.SessionModel;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionAuthenticationException;
import com.alfredcode.socialWebsite.service.session.exception.FailedSessionUpdateException;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {



    /*
     * 
     * Mockito doesnt allow you to stuff method calls that are not used
     * 
     * I need to use a real SessionService, and give it a mock sessionDao
     */

    @Mock
    SessionDAO sessionDao;

    @InjectMocks
    SessionService sessionService;

    // valid, non expired, in database
    private static final String validNotExpiredPersistedSessionId = "validNonExpiredSession";
    // valid, not in the database
    private static final String validNotPersistedSessionId = "validNonExistentSessionId";
    // valid, expired, in database
    private static final String validExpiredPersistedSessionId = "validExpiredSessionId";
    // invalid, contains characters
    private static final String invalidCorrectSessionId = "invalidSessionId";
    // invalid, null
    private static final String invalidNullSessionId = null;
    // invalid, blank
    private static final String invalidBlankSessionId = "    ";
    // invalid, empty
    private static final String invalidEmptySessionId = "";

    // valid, non expired
    private static final SessionModel validSessionModel = new SessionModel("0", "username", new Date().getTime() + 999999, new Date().getTime() + 9999999);
    // valid, expired
    private static final SessionModel expiredSessionModel = new SessionModel("0", "username", 0L, 0L);

    @Test
    void authenticateSessionUnitTest() {

        when(sessionDao.getSessionById(validNotExpiredPersistedSessionId))
        .thenReturn(validSessionModel);

        when(sessionDao.getSessionById(validNotPersistedSessionId))
        .thenReturn(null);

        when(sessionDao.getSessionById(validExpiredPersistedSessionId))
        .thenReturn(expiredSessionModel);

        when(sessionDao.getSessionById(invalidCorrectSessionId))
        .thenReturn(null);


        assertAll(() -> {

            // given a valid, not expired, presisted session ID: no exceptions should trigger
            assertDoesNotThrow(() -> {
                sessionService.authenticateSession(validNotExpiredPersistedSessionId);
            });

            // given a valid, not expired, not persisted session ID: Ex should trigger
            assertThrows(FailedSessionAuthenticationException.class, () -> {
                sessionService.authenticateSession(validNotPersistedSessionId);
            });

            // given a valid, expired, persisted session ID: Ex should trigger
            assertThrows(FailedSessionAuthenticationException.class, () -> {
                sessionService.authenticateSession(validExpiredPersistedSessionId);
            });

            // given an invalid, syntactically correct session ID: throw ex
            assertThrows(FailedSessionAuthenticationException.class, () -> {
                sessionService.authenticateSession(invalidCorrectSessionId);
            });

            // given an invalid, null session ID: throw ex
            assertThrows(IllegalArgumentException.class, () -> {
                sessionService.authenticateSession(invalidNullSessionId);
            });

            // given an invalid, blank session ID: throw ex
            assertThrows(IllegalArgumentException.class, () -> {
                sessionService.authenticateSession(invalidBlankSessionId);
            });

            // given an invalid, empty session ID: throw ex
            assertThrows(IllegalArgumentException.class, () -> {
                sessionService.authenticateSession(invalidEmptySessionId);
            });

        });
    }

}
