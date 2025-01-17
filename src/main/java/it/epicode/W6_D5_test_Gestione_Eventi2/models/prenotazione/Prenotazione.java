package it.epicode.W6_D5_test_Gestione_Eventi2.models.prenotazione;

import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import it.epicode.W6_D5_test_Gestione_Eventi2.models.evento.Evento;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private LocalDate dataPrenotazione;

    private int numeroPosti;


}