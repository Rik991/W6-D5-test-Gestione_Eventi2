package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ORGANIZER')or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Evento> creaEvento(@RequestBody EventoRequest eventoRequest) {
        Evento evento = eventoService.creaEvento(eventoRequest);
        return new ResponseEntity<>(evento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<Evento> modificaEvento(@PathVariable Long id, @RequestBody EventoRequest eventoRequest) {
        Evento evento = eventoService.modificaEvento(id, eventoRequest);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Evento> cancellaEvento(@PathVariable Long id) {
        Evento evento = eventoService.cancellaEvento(id);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }



}



