package com.sparc.products.org.graph.node;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import com.sparc.products.org.graph.relationship.OrganizationRole;

import java.util.HashSet;
import java.util.Set;

/**
 * Employee: Dayel Ostraco
 * Date: 10/22/12
 * Time: 4:24 PM
 */
@NodeEntity
public class Employee {

    @GraphId
    private Long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;

    @Fetch
    @RelatedTo(type = "ROLE_IN", direction = Direction.INCOMING)
    private Set<Organization> organizations;

    @Fetch
    @RelatedToVia(type = "ROLE_IN", direction = Direction.INCOMING)
    private Set<OrganizationRole> organizationRoles;

    public Employee() {

    }

    public Employee(String firstName, String middleName, String lastName, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Organization> getOrganization() {
        return organizations;
    }

    public void setOrganization(Set<Organization> organization) {
        this.organizations = organization;
    }

    public Set<OrganizationRole> getOrganizationRoles() {
        return organizationRoles;
    }

    public void setOrganizationRoles(Set<OrganizationRole> organizationRoles) {
        this.organizationRoles = organizationRoles;
    }

    public OrganizationRole roleIn(Organization organization, String role){
        OrganizationRole organizationRole = new OrganizationRole(organization, this, role);

        if(organizationRoles==null){
            organizationRoles = new HashSet<OrganizationRole>();
        }

        this.organizationRoles.add(organizationRole);

        return organizationRole;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Employee){
            Employee employee = (Employee) o;
            return id.equals(employee.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        if(id==null){
            return super.hashCode();
        }
        return id.intValue();
    }
}