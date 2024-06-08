package com.alfredcode.socialWebsite.Controllers.Frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alfredcode.socialWebsite.Services.UIService;
import com.alfredcode.socialWebsite.Tables.UIButton;

@Controller
@RequestMapping("/ui/")
public class UIController {
    private static final Logger logger = LoggerFactory.getLogger(UIController.class);
    private UIService uiService = new UIService();
    
    @GetMapping("buttons/type/{type}")
    @ResponseBody
    public UIButton[] getButtonsByType(@PathVariable("type") String type) {

        // ask frontendDAO for those buttons
        logger.warn("TYPE: " + type);

        return uiService.getButtonsByType(type);
        //return "{\"message\": \"hi\"}";
    }
}
