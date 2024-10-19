package com.hitecart.deliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@EntityListeners(AuditingEntityListener.class)
public class Role extends AbstractEntity  {
    @Column(unique=true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<Utilisateur> users;
}
