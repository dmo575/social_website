package com.alfredcode.socialWebsite.Services;

import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.Models.SessionModel;

/*
 * Manages business logic for the session entities
 */
@Service
public class SessionService {
    private SessionDAO sessionDao = new SessionDAO();


    public SessionModel addSession(SessionModel sessionModel) {
        return sessionDao.addSession(sessionModel);
    }

    public SessionModel getSessionById(String sessionId) {
        return sessionDao.getSessionById(sessionId);
    }

    public SessionModel getSessionByUsername(String username) {
        return sessionDao.getSessionByUsername(username);
    }

    public SessionModel updateSessionWithId(String sessionId, SessionModel sessionModel) {
        SessionModel newSession = sessionDao.updateSessionWithId(sessionId, sessionModel, true);

        if(newSession == null) {
            return sessionDao.addSession(sessionModel);
        }

        return newSession;
    }

    public Boolean removeSessionWithId(String sessionId) {

        return sessionDao.removeSessionWithId(sessionId);
    }
}
