package com.fiktionmaps.fiktionmaps.resource;

import com.fiktionmaps.fiktionmaps.mapper.PlaceMapper;
import com.fiktionmaps.fiktionmaps.model.Place;
import com.fiktionmaps.fiktionmaps.repository.PlaceRepository;
import com.fiktionmaps.fiktionmaps.service.PlaceService;
import com.fiktionmaps.fiktionmaps.service.dto.PlaceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceResource {

    private PlaceRepository placeRepsitory;

    private PlaceService placeService;

    private PlaceMapper placeMapper;

    public PlaceResource(PlaceRepository placeRepository, PlaceMapper placeMapper, PlaceService placeService){
        this.placeRepsitory = placeRepository;
        this.placeMapper = placeMapper;
        this.placeService = placeService;
    }

    @GetMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PlaceDTO>> getPlaces(){
        List<Place> places = placeRepsitory.findAll();
        return new ResponseEntity<>(placeMapper.toDtoList(places), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.delete(id);
        return ResponseEntity.ok().build();
    }
}
