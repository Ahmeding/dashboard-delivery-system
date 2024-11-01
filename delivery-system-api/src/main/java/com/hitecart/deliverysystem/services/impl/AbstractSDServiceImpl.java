package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.services.IAbstractDSService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public class AbstractSDServiceImpl<Entity,EntityDto,IEntityRepository extends JpaRepository<Entity,Integer>>
        implements IAbstractDSService<Entity, EntityDto,IEntityRepository> {

        private final IEntityRepository entityRepository;
        private final Entity entityClass;
        private final EntityDto entityDtoClass;

        public AbstractSDServiceImpl(IEntityRepository entityRepository, Entity entityClass, EntityDto entityDtoClass) {
            this.entityRepository = entityRepository;
            this.entityClass = entityClass;
            this.entityDtoClass = entityDtoClass;
        }
    @Override
    public EntityDto create(EntityDto entityDto) {
        Entity entity = this.entityRepository.saveAndFlush(entityClass);

            return null;
    }

    @Override
    public EntityDto update(EntityDto entityDto) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<EntityDto> findAll() {
        return List.of();
    }

    @Override
    public EntityDto findById(Integer id) {
        return null;
    }
}
