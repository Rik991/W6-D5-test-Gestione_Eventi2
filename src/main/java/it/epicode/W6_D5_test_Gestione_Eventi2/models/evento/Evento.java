package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;

import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Il titolo è obbligatorio")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    @FutureOrPresent
    private LocalDate data;

    @NotBlank(message = "Il luogo è obbligatorio")
    private String luogo;

    @Column(name = "posti_disponibili")
    @NotNull
    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private AppUser organizer;

}
