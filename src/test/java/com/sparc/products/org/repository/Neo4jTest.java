package com.sparc.products.org.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.sparc.common.utils.RandomUtils;
import com.sparc.products.org.graph.node.Employee;
import com.sparc.products.org.graph.node.Organization;
import com.sparc.products.org.graph.relationship.OrganizationRole;

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

    @Test
    @Transactional
    public void testCompanyWithMultipleEmployees() throws Exception {

        //Create Random Nodes
        Organization organization = createRandomOrganization();
        Set<Employee> employees = createRandomEmployees(RandomUtils.getRandomIntBetween(2,20));

        //Create Random relationships for each random Employee and test
        for(Employee employee : employees){

            //Create Random OrganizationRoles
            Set<OrganizationRole> organizationRoles =
                    createRandomOrganizationRoles(organization, employee, RandomUtils.getRandomIntBetween(1, 10));

            //Retrieve latest version of Employee and test
            Employee retrievedEmployee = template.findOne(employee.getId(), Employee.class);
            assertTrue("retrieved employee has all their OrganizationRoles persisted",
                    retrievedEmployee.getOrganizationRoles().containsAll(organizationRoles));

            //Retrieve latest version of Organization and test
            Organization retrievedOrganization = template.findOne(organization.getId(), Organization.class);
            assertTrue("retrieved Organization contains all OrganizationRoles",
                    retrievedOrganization.getOrganizationRoles().containsAll(organizationRoles));
            assertTrue("retrieved Organization contains the current Employee", retrievedOrganization.getEmployees().contains(employee));
        }
    }

    @Transactional
    private Organization createRandomOrganization(){
        return template.save(new Organization(RandomUtils.getRandomString(4, 25)));
    }

    @Transactional
    private Set<Organization> createRandomOrganizations(int numberRequested){

        Set<Organization> randomOrganizations = new HashSet<Organization>();

        for(int i = 0 ; i<numberRequested ; i++){
            randomOrganizations.add(createRandomOrganization());
        }

        return randomOrganizations;
    }

    @Transactional
    private Employee createRandomEmployee(){
        String firstName = RandomUtils.getRandomString(3, 5);
        String middleName = RandomUtils.getRandomString(3, 15);
        String lastName = RandomUtils.getRandomString(3, 20);
        String emailAddress = RandomUtils.getRandomString(15, 30);

        return template.save(new Employee(firstName, middleName, lastName, emailAddress));
    }

    @Transactional
    private Set<Employee> createRandomEmployees(int numberRequested){

        Set<Employee> randomEmployees = new HashSet<Employee>();

        for(int i = 0 ; i<numberRequested ; i++){
            randomEmployees.add(createRandomEmployee());
        }

        return randomEmployees;
    }

    @Transactional
    private OrganizationRole createRandomOrganizationRole(Organization organization, Employee employee){
        return template.save(new OrganizationRole(organization, employee, RandomUtils.getRandomString(3, 25)));
    }

    @Transactional
    private Set<OrganizationRole> createRandomOrganizationRoles(Organization organization, Employee employee, int numberRequested){

        Set<OrganizationRole> organizationRoles = new HashSet<OrganizationRole>();

        for(int i = 0 ; i<numberRequested ; i++){
            organizationRoles.add(createRandomOrganizationRole(organization, employee));
        }

        return organizationRoles;
    }
}