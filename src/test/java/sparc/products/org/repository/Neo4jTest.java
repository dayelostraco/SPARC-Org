package sparc.products.org.repository;

import org.apache.commons.lang.RandomStringUtils;
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
import static org.junit.Assert.assertTrue;

/**
 * User: Dayel Ostraco
 * Date: 10/22/12
 * Time: 10:19 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/sparc-org-test-context.xml"})
public class Neo4jTest {

    @Autowired
    Neo4jTemplate template;

    @Test
    @Transactional
    public void testCreateOrganization() throws Exception {
        Organization organization = createRandomOrganization();
        Organization organizationRetrieved = template.findOne(organization.getId(), Organization.class);
        assertEquals("retrieved organization matches persisted one", organization.getId(), organizationRetrieved.getId());
        assertEquals("retrieved organization title matches", organization.getOrganizationName(), organizationRetrieved.getOrganizationName());
    }

    @Test
    @Transactional
    public void testCreateEmployee() throws Exception {
        Employee employee = createRandomEmployee();
        Employee employeeRetrieved = template.findOne(employee.getId(), Employee.class);
        assertEquals("retrieved employee matches persisted one", employee.getId(), employeeRetrieved.getId());
        assertEquals("retrieved employee name matches", employee.getFirstName(), employeeRetrieved.getFirstName());
    }

    @Test
    @Transactional
    public void testCreateRichRelationship() throws Exception {

        //Create Nodes and Relationships
        Organization organization = createRandomOrganization();
        Employee employee = createRandomEmployee();
        OrganizationRole organizationRole = createRandomOrganizationRole(organization, employee);

        //Fetch eagerly loaded Employee and Organization Nodes
        Employee retrievedEmployee = template.findOne(employee.getId(), Employee.class);
        Organization retrievedOrganization = template.findOne(organization.getId(), Organization.class);

        //Tests OrganizationRole was persisted
        assertTrue("retrieved employee has the related OrganizationRole attached", retrievedEmployee.getOrganizationRoles().contains(organizationRole));

        //Tests Employee to Organization Relationship
        assertTrue("retrieved employee has the related Organization attached",
                retrievedEmployee.getOrganization().contains(retrievedOrganization));

        //Tests Organization to Employee Relationship
        assertTrue("retrieved organization has the related Employee attached",
                retrievedOrganization.getEmployees().contains(retrievedEmployee));
    }

    private Organization createRandomOrganization(){
        return template.save(new Organization(RandomStringUtils.randomAlphanumeric(20)));
    }

    private Set<Organization> createRandomOrganizations(int numberRequested){

        Set<Organization> randomOrganizations = new HashSet<Organization>();

        for(int i = 0 ; i<numberRequested ; i++){
            randomOrganizations.add(createRandomOrganization());
        }

        return randomOrganizations;
    }

    private Employee createRandomEmployee(){
        String firstName = RandomStringUtils.randomAlphanumeric(10);
        String middleName = RandomStringUtils.randomAlphanumeric(10);
        String lastName = RandomStringUtils.randomAlphanumeric(10);
        String emailAddress = RandomStringUtils.randomAlphanumeric(25);

        return template.save(new Employee(firstName, middleName, lastName, emailAddress));
    }

    private Set<Employee> createRandomEmployees(int numberRequested){

        Set<Employee> randomEmployees = new HashSet<Employee>();

        for(int i = 0 ; i<numberRequested ; i++){
            randomEmployees.add(createRandomEmployee());
        }

        return randomEmployees;
    }

    private OrganizationRole createRandomOrganizationRole(Organization organization, Employee employee){
        return template.createRelationshipBetween(organization, employee, OrganizationRole.class, "ROLE_IN", true);
    }

    private Set<OrganizationRole> createRandomOrganizationRoles(Organization organization, Employee employee, int numberRequested){

        Set<OrganizationRole> organizationRoles = new HashSet<OrganizationRole>();

        for(int i = 0 ; i<numberRequested ; i++){
            organizationRoles.add(createRandomOrganizationRole(organization, employee));
        }

        return organizationRoles;
    }
}