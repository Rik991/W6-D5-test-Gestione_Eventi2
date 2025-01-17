package it.epicode.W6_D5_test_Gestione_Eventi2.models.prenotazione;


import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUserRepository;
import it.epicode.W6_D5_test_Gestione_Eventi2.exceptions.PrenotazioneException;
import it.epicode.W6_D5_test_Gestione_Eventi2.models.evento.Evento;
import it.epicode.W6_D5_test_Gestione_Eventi2.models.evento.EventoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoRepository eventoRepository;
    private final AppUserRepository appUserRepository;


    //ottengo l'id dell'utente loggato come faccio nell'evento
    private AppUser getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    public List<Prenotazione> getPrenotazioniUtente() {
        AppUser user = getLoggedInUser();
        return prenotazioneRepository.findByUserId(user.getId());
    }


    public Prenotazione effettuaPrenotazione(@Valid PrenotazioneRequest prenotazioneRequest) {

        Evento evento = eventoRepository.findById(prenotazioneRequest.getEventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        //controllo se ci sono i posti disponibili
        if (evento.getPostiDisponibili() < prenotazioneRequest.getNumeroPosti()) {
            throw new PrenotazioneException("Posti non disponibili");
        }

        //creo l'evento
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEvento(evento);
        prenotazione.setUser(getLoggedInUser());
        prenotazione.setDataPrenotazione(LocalDate.now());
        prenotazione.setNumeroPosti(prenotazioneRequest.getNumeroPosti());

        //sottraggo i posti prenotati a quelli disponibili
        evento.setPostiDisponibili(evento.getPostiDisponibili() - prenotazioneRequest.getNumeroPosti());
        //aggiorno i posti
        eventoRepository.save(evento);

        return prenotazioneRepository.save(prenotazione);

    }

    public void cancellaPrenotazioneUtente(Long prenotazioneId) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        AppUser loggedInUser = getLoggedInUser();

        // Verifica che sia la prenotazione dell'utente
        if (!prenotazione.getUser().getId().equals(loggedInUser.getId())) {
            throw new PrenotazioneException("Non puoi cancellare prenotazioni di altri utenti");
        }

        // Ripristina i posti disponibili
        Evento evento = prenotazione.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + prenotazione.getNumeroPosti());
        eventoRepository.save(evento);

        // Cancella la prenotazione
        prenotazioneRepository.delete(prenotazione);
    }

}
