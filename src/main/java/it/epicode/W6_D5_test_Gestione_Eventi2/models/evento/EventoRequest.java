package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;


import lombok.Data;

import java.time.LocalDate;

@Data
public class EventoRequest {

    private String titolo;

    private String descrizione;

    private LocalDate data;

    private String luogo;

    private int postiDisponibili;

    private Long organizerId;


}
