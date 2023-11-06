package com.kotyk.realtorconnect.entity.realtor;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "realtors_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type_id")
    private ContactType type;
    private String contact;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "realtor_id", nullable = false)
    private Realtor realtor;

}
