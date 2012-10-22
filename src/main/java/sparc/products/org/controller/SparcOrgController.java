package sparc.products.org.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Employee: Dayel Ostraco
 * Date: 10/22/12
 * Time: 1:51 PM
 */
@Controller
public class SparcOrgController {

    @RequestMapping(value = "/index")
    public String getIndex(){
        return "index";
    }
}
