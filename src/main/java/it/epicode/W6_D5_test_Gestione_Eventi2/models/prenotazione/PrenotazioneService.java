package it.epicode.W6_D5_test_Gestione_Eventi2.models.prenotazione;


import com.cloudinary.provisioning.Account;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUserRepository;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.Role;
import it.epicode.W6_D5_test_Gestione_Eventi2.models.evento.Evento;
import it.epicode.W6_D5_test_Gestione_Eventi2.models.evento.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoRepository eventoRepository;
    private final AppUserRepository appUserRepository;


    //ottengo l'id dell'utente loggato come faccio nell'evento
    private AppUser getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public List<Prenotazione> getPrenotazioniUtente() {
        AppUser user = getLoggedInUser();
        return prenotazioneRepository.findByUserId(user.getId());
    }


    public Prenotazione effettuaPrenotazione(PrenotazioneReuqest prenotazioneReuqest) {

        Evento evento = eventoRepository.findById(prenotazioneReuqest.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        //controllo se ci sono i posti disponibili
        if (evento.getPostiDisponibili() < prenotazioneReuqest.getNumeroPosti()) {
            throw new RuntimeException("Posti non disponibili");
        }

        //creo l'evento
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEvento(evento);
        prenotazione.setUser(getLoggedInUser());
        prenotazione.setDataPrenotazione(LocalDate.now());
        prenotazione.setNumeroPosti(prenotazioneReuqest.getNumeroPosti());

        //sottraggo i posti prenotati a quelli disponibili
        evento.setPostiDisponibili(evento.getPostiDisponibili() - prenotazioneReuqest.getNumeroPosti());
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
            throw new AccessDeniedException("Non puoi cancellare prenotazioni di altri utenti");
        }

        // Ripristina i posti disponibili
        Evento evento = prenotazione.getEvento();
        evento.setPostiDisponibili(evento.getPostiDisponibili() + prenotazione.getNumeroPosti());
        eventoRepository.save(evento);

        // Cancella la prenotazione
        prenotazioneRepository.delete(prenotazione);
    }

}
