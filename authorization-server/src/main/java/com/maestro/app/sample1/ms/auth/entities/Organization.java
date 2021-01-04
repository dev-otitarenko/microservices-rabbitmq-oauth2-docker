package com.maestro.app.sample1.ms.auth.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseGuidEntity {
    private static final long serialVersionUID = 1L;

    private String idPar;
    private String name;
    private String centre;
    private Integer orgLevel;
}
