package sparc.products.org.graph.node;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

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
