package com.mapToFiction.mapToFiction.service.impl;
import com.mapToFiction.mapToFiction.mapper.FictionMapper;
import com.mapToFiction.mapToFiction.mapper.SceneMapper;
import com.mapToFiction.mapToFiction.model.Fiction;
import com.mapToFiction.mapToFiction.model.Location;
import com.mapToFiction.mapToFiction.model.Scene;
import com.mapToFiction.mapToFiction.repository.FictionRepository;
import com.mapToFiction.mapToFiction.repository.LocationRepository;
import com.mapToFiction.mapToFiction.repository.SceneRepository;
import com.mapToFiction.mapToFiction.service.FictionService;
import com.mapToFiction.mapToFiction.service.dto.FictionDTO;
import com.mapToFiction.mapToFiction.service.dto.SceneDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FictionServiceImpl implements FictionService {
    private final FictionRepository fictionRepository;
    private final SceneRepository sceneRepository;
    private final LocationRepository locationRepository;
    private final FictionMapper fictionMapper;
    private final SceneMapper sceneMapper;


    public FictionServiceImpl(FictionRepository fictionRepository, SceneRepository sceneRepository, LocationRepository locationRepository, FictionMapper fictionMapper, SceneMapper sceneMapper) {
        this.fictionRepository = fictionRepository;
        this.sceneRepository = sceneRepository;
        this.locationRepository = locationRepository;
        this.fictionMapper = fictionMapper;
        this.sceneMapper = sceneMapper;

    }

    @Override
    public List<FictionDTO> getAll() {
        List<Fiction> fictions = fictionRepository.findAll();
        return fictionMapper.toDtoList(fictions);
    }

    @Override
    public Fiction getById(Long id) {
        return fictionRepository.findById(id).orElse(null);
    }


    @Override
    public FictionDTO create(FictionDTO fictionDto) {
        Fiction fiction = fictionMapper.toEntity(fictionDto);
        Fiction savedFiction = fictionRepository.save(fiction);
        return fictionMapper.toDto(savedFiction);
    }

    @Override
    public Fiction update(Fiction fiction) {
        return fictionRepository.save(fiction);
    }

    @Override
    public Fiction update(Long id, Fiction fiction) {
        return fictionRepository.findById(id)
                .map(f -> {
                    f.setName(fiction.getName());
                    f.setType(fiction.getType());
                    f.setScenes(fiction.getScenes());
                    return fictionRepository.save(fiction);
                })
                .orElseThrow(() -> new IllegalArgumentException("Fiction not found with id " + id));
    }

    @Override
    public Fiction findByName(String name) {
        return fictionRepository.findByName(name);
    }

    @Override
    public FictionDTO addScene(Long id, SceneDTO sceneDto){

        Optional<Fiction> fictionOpt = fictionRepository.findById(id);
        if (!fictionOpt.isPresent()) {
            throw new NoSuchElementException("No Fiction found with id " + id);
        }
        Fiction fiction = fictionOpt.get();

        Scene scene = sceneMapper.toEntity(sceneDto);
        scene.setFiction(fiction);
        sceneRepository.save(scene);

        return fictionMapper.toDto(fiction);
    }

    @Override
    public Scene addLocationToScene(Long fictionId, Long sceneId, Location location) {

        Optional<Fiction> fictionOpt = fictionRepository.findById(fictionId);
        if (!fictionOpt.isPresent()) {
            throw new NoSuchElementException("No Fiction found with id " + fictionId);
        }

        Scene scene = sceneRepository.findById(sceneId).get();
        locationRepository.save(location);
        scene.setLocation(location);
        return sceneRepository.save(scene);
    }

    @Override
    public void deleteFiction(Long id){
        fictionRepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return fictionRepository.existsByName(name);
    }
}