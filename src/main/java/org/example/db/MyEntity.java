package org.example.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MyEntity {

    @Id
    @GeneratedValue
    private int id;
}
