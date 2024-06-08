package com.alfredcode.socialWebsite.Services;

import com.alfredcode.socialWebsite.DAO.UIDAO;
import com.alfredcode.socialWebsite.Tables.UIButton;

public class UIService {
    private UIDAO feDao = new UIDAO();
    
    public UIButton[] getButtonsByType(String type) {

        return feDao.GetButtonsByType(type);
    }
}
