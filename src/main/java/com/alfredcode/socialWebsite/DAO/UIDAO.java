package com.alfredcode.socialWebsite.DAO;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.alfredcode.socialWebsite.Database;
import com.alfredcode.socialWebsite.Tables.UIButton;

public class UIDAO {
    private Database db = Database.getInstance();

    public UIButton[] GetButtonsByType(String type) {
        ArrayList<UIButton> buttons = new ArrayList<UIButton>();

        for(UIButton b : db.button) {
            if(b.type.equals(type))
                buttons.add(b);
        }
        
        return buttons.toArray(new UIButton[0]);
    }
}
