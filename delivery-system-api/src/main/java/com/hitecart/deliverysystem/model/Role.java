package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitecart.deliverysystem.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "role")
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@EntityListeners(AuditingEntityListener.class)
public class Role extends AbstractEntity  {

    @Enumerated(EnumType.STRING)
    @Column(unique=true)
    private RoleType name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;
}
