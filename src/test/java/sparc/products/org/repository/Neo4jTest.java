package sparc.products.org.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import sparc.products.org.model.Organization;

import static org.junit.Assert.assertEquals;

/**
 * User: Dayel Ostraco
 * Date: 10/22/12
 * Time: 10:19 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/sparc-org-text-context.xml"})
public class Neo4jTest {

    @Autowired
    Neo4jTemplate template;

    @Test
    @Transactional
    public void testTransaction() throws Exception {

        Organization sparc = new Organization();
        sparc.setOrganizationName("SPARC LLC");

        sparc = template.save(sparc);
        Organization sparcRetrieved = template.findOne(sparc.getId(), Organization.class);
        assertEquals("retrieved organization matches persisted one", sparc.getId(), sparcRetrieved.getId());
        assertEquals("retrieved organization title matches", sparc.getOrganizationName(), sparcRetrieved.getOrganizationName());
    }
}
