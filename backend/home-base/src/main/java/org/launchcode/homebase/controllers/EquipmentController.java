package org.launchcode.homebase.controllers;

import org.launchcode.homebase.data.EquipmentRepository;
import org.launchcode.homebase.data.UserRepository;
import org.launchcode.homebase.exception.ResourceNotFoundException;
import org.launchcode.homebase.models.Equipment;
import org.launchcode.homebase.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.launchcode.homebase.models.dto.EquipmentUserDTO;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:8081"})
@RequestMapping("/api")
public class EquipmentController {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();

        if (equipment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") int id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Equipment with id = " + id));

        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody EquipmentUserDTO equipmentDTO) {
        Equipment _equipment = new Equipment(equipmentDTO.getName(), equipmentDTO.getFilters(), equipmentDTO.getFilterLifeDays());
        equipmentRepository.save(_equipment);

        User user = userRepository.findById(equipmentDTO.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + equipmentDTO.getUserId()));
        user.getEquipments().add(_equipment);
        _equipment.getUsers().add(user);

        userRepository.save(user);

        return new ResponseEntity<>(_equipment, HttpStatus.CREATED);
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") int id, @RequestBody Equipment equipment) {
        Equipment _equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Equipment with id = " + id));

        _equipment.setName(equipment.getName());
        _equipment.setFilters(equipment.getFilters());
        _equipment.setFilterLifeDays(equipment.getFilterLifeDays());

        return new ResponseEntity<>(equipmentRepository.save(_equipment), HttpStatus.OK);
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<HttpStatus> deleteEquipment(@PathVariable("id") int id) {
        equipmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
