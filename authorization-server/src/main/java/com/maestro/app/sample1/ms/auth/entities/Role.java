package com.maestro.app.sample1.ms.auth.entities;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ROLES")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseIdEntity {
	private static final long serialVersionUID = 1L;

	private String name;
}
