package uz.itransition.collectin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.itransition.collectin.service.marker.Creationable;
import uz.itransition.collectin.service.marker.Modifiable;

import javax.validation.Valid;


public interface CRUDController<ID, C extends Creationable, U extends Modifiable> {

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid C c);

    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable ID id);

    @PutMapping("/{id}")
    ResponseEntity<?> modify(@PathVariable ID id, @RequestBody @Valid U u);

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable ID id);


}
