package com.alfredcode.socialWebsite.service;

import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.model.SessionModel;

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

    /////////////////////////////////////////////////////////////////////////////////////////

    public Boolean isSessionValid(String sessionId) {
        return false;
    }
}
