package com.hitecart.deliverysystem.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAbstractDSService<Entity,EntityDto,IEntityRepository extends JpaRepository<Entity,Integer>> {
    public EntityDto create(EntityDto entityDto);
    public EntityDto update(EntityDto entityDto);
    public void deleteById(Integer id);
    public List<EntityDto> findAll();
    public EntityDto findById(Integer id);
}
