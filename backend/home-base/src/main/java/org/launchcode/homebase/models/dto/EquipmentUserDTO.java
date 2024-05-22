package org.launchcode.homebase.models.dto;

import org.launchcode.homebase.models.Filter;

import java.util.List;

public class EquipmentUserDTO {
    private String name;
    private List<Filter> filters;
    private int filterLifeDays;
    private int userId;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
