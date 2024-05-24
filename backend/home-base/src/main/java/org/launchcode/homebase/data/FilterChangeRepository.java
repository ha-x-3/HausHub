package org.launchcode.homebase.data;


import org.launchcode.homebase.models.FilterChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterChangeRepository extends JpaRepository<FilterChangeHistory, Integer> {

    List<FilterChangeHistory> findByEquipmentId(Long equipmentId);

}
