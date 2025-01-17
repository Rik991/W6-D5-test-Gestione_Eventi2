package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;

import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String titolo;

    private String descrizione;

    private LocalDate data;

    private String luogo;

    @Column(name = "posti_disponibili")
    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private AppUser organizer;

}