package com.auca.clinic.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "office")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private int officeNumber;

    public Office() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(int officeNumber) { this.officeNumber = officeNumber; }
}
