package org.example.proyectoReservaa.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
    @Id
    @GeneratedValue(generator= "system-uuid")
    @GenericGenerator(name="system-uuid", strategy="uuid" )
    @Column(length=32)
    @Hidden
    private String id;
}
