package it.epicode.W6_D5_test_Gestione_Eventi2.models.prenotazione;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;


    @GetMapping("/mie-prenotazioni")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniUtente() {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniUtente();
        return ResponseEntity.ok(prenotazioni);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Prenotazione> effettuaPrenotazione(@RequestBody PrenotazioneReuqest prenotazioneReuqest) {

        Prenotazione prenotazione = prenotazioneService.effettuaPrenotazione(prenotazioneReuqest);

        return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
    }

    @DeleteMapping("/mie-prenotazioni/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> cancellaPrenotazioneUtente(@PathVariable Long id) {
        prenotazioneService.cancellaPrenotazioneUtente(id);
        return ResponseEntity.noContent().build();
    }

}
