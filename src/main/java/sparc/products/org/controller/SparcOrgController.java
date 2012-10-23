package sparc.products.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Employee: Dayel Ostraco
 * Date: 10/22/12
 * Time: 1:51 PM
 */
@Controller
public class SparcOrgController {

    @Autowired
    Neo4jTemplate template;

    @RequestMapping(value = "/index")
    public String getIndex(){
        return "index";
    }
}
