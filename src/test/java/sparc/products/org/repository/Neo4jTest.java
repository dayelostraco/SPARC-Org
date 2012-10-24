package sparc.products.org.repository;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import sparc.products.org.graph.node.Employee;
import sparc.products.org.graph.node.Organization;
import sparc.products.org.graph.relationship.OrganizationRole;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    private static Set<Organization> organizations;
    private static Set<Employee> employees;
    private static Set<OrganizationRole> organizationRoles;

    @BeforeClass
    public static void instantiateCollections(){
        organizations = new HashSet<Organization>();
        employees = new HashSet<Employee>();
        organizationRoles = new HashSet<OrganizationRole>();
    }

    @Test
    @Transactional
    public void testNodeRelationshipModel() throws Exception {
        testCreateOrganization();
        testCreateEmployee();
        testCreateRichRelationship();
        testMultipleRelationships();
    }

    @Transactional
    public void testCreateOrganization() throws Exception {
        Organization organization = template.save(new Organization("SPARC LLC"));
        Organization organizationRetrieved = template.findOne(organization.getId(), Organization.class);
        assertEquals("retrieved organization matches persisted one", organization.getId(), organizationRetrieved.getId());
        assertEquals("retrieved organization title matches", organization.getOrganizationName(), organizationRetrieved.getOrganizationName());

        organizations.add(organizationRetrieved);
    }

    @Transactional
    public void testCreateEmployee() throws Exception {
        Employee employee = template.save(new Employee("Dayel", "Blake", "Ostraco", "dayel.ostraco@sparcedge.com"));
        Employee employeeRetrieved = template.findOne(employee.getId(), Employee.class);
        assertEquals("retrieved employee matches persisted one", employee.getId(), employeeRetrieved.getId());
        assertEquals("retrieved employee name matches", employee.getFirstName(), employeeRetrieved.getFirstName());

        employees.add(employeeRetrieved);
    }

    @Transactional
    public void testCreateRichRelationship() throws Exception {
        Organization organization = (Organization) organizations.toArray()[0];
        Employee employee = (Employee) employees.toArray()[0];

        OrganizationRole organizationRole = template.save(new OrganizationRole(organization, employee, "Tech Lead"));
        assertNotNull("retrieved role matches persisted one", organizationRole.getId());

        organizationRoles.add(organizationRole);

        Employee retrievedEmployee = template.findOne(employee.getId(), Employee.class);
        Organization retrievedOrganization = template.findOne(organization.getId(), Organization.class);
        OrganizationRole retrievedRole = (OrganizationRole) retrievedEmployee.getOrganizationRoles().toArray()[0];
        assertEquals("retrieved employee has the persisted relationship to organization", organizationRole.getRole(), retrievedRole.getRole());
        assertEquals("retrieved organization fetched all employees with organization", retrievedOrganization.getEmployees().size(), employees.size());
    }

    @Transactional
    public void testMultipleRelationships() throws Exception {
        Organization organization = (Organization) organizations.toArray()[0];
        Employee employee = (Employee) employees.toArray()[0];

        OrganizationRole organizationRole2 = template.save(new OrganizationRole(organization, employee, "Director"));
        assertTrue("persisted second organization role", organizationRole2.getId()!=0);
        organizationRoles.add(organizationRole2);
        OrganizationRole organizationRole3 = template.save(new OrganizationRole(organization, employee, "Senior Engineer"));
        assertTrue("persisted third organization role", organizationRole3.getId()!=0);
        organizationRoles.add(organizationRole3);

        Employee retrievedEmployee = template.findOne(employee.getId(), Employee.class);
        assertEquals("retrieved employee has all relationships mapped", retrievedEmployee.getOrganizationRoles().size(), organizationRoles.size());
    }
}
