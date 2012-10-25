package com.sparc.products.org.graph.node;

import com.sparc.products.org.graph.relationship.OrganizationRole;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.Set;

/**
 * Employee: Dayel Ostraco
 * Date: 10/22/12
 * Time: 4:23 PM
 */
@NodeEntity
public class Organization {

    @GraphId
    private Long id;
    private String organizationName;

    @Fetch
    @RelatedTo(type = "ROLE_IN", direction = Direction.OUTGOING)
    private Set<Employee> employees;

    @Fetch
    @RelatedToVia(type = "ROLE_IN", direction = Direction.OUTGOING)
    private Set<OrganizationRole> organizationRoles;

    public Organization() {

    }

    public Organization(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<OrganizationRole> getOrganizationRoles() {
        return organizationRoles;
    }

    public void setOrganizationRoles(Set<OrganizationRole> organizationRoles) {
        this.organizationRoles = organizationRoles;
    }

    @Override
    public boolean equals(Object o) {

        if(o instanceof Organization){
            Organization organization = (Organization) o;
            return id.equals(organization.getId());
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
