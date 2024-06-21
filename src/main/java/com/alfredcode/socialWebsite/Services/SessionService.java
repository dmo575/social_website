package com.alfredcode.socialWebsite.Services;

import com.alfredcode.socialWebsite.DAO.SessionDAO;
import com.alfredcode.socialWebsite.Models.SessionModel;

public class SessionService {
    private SessionDAO sessionDao = new SessionDAO();

    public SessionModel addSession(SessionModel sessionModel) {

    }

    public SessionModel getSessionById(String sessionId) {
        return null;
    }

    public SessionModel getSessionByUsername(String username) {
        return null;
    }

    // null if nothing got modified.
    public SessionModel updateSessionWithId(String sessionId, SessionModel sessionModel) {

    }

    public Boolean removeSessionWithId(String sessionId) {

    }
}
