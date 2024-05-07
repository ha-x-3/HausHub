package org.launchcode.homebase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Equipment extends AbstractEntity {

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @OneToMany(mappedBy = "equipment")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Filter> filters;

    @NotNull(message = "Filter life is required.")
    @Positive(message = "Filter life must be a positive number.")
    private int filterLifeDays;

    @ManyToMany(mappedBy = "equipments",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Equipment(String name, List<Filter> filters, int filterLifeDays) {
        this.name = name;
        this.filters = filters;
        this.filterLifeDays = filterLifeDays;
    }

    public Equipment() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public int getFilterLifeDays() {
        return filterLifeDays;
    }

    public void setFilterLifeDays(int filterLifeDays) {
        this.filterLifeDays = filterLifeDays;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return name;
    }
}
