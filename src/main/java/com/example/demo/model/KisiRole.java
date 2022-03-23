package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roller")
@Getter
@Setter
@NoArgsConstructor
public class KisiRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERoller name;


    public KisiRole(ERoller name){
        this.name = name;
    }

}
