package sparc.products.org.graph.relationship;

import org.springframework.data.neo4j.annotation.*;
import sparc.products.org.graph.node.Employee;
import sparc.products.org.graph.node.Organization;

/**
 * User: Dayel Ostraco
 * Date: 10/22/12
 * Time: 7:50 PM
 */
@RelationshipEntity(type = "ROLE_IN")
public class OrganizationRole {

    @GraphId
    private Long id;

    @Fetch
    @StartNode
    private Organization organization;

    @Fetch
    @EndNode
    private Employee employee;

    private String role;

    public OrganizationRole() {

    }

    public OrganizationRole(Organization organization, Employee employee, String role) {
        this.organization = organization;
        this.employee = employee;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
