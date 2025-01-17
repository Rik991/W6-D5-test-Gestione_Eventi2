package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;


import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUserRepository;
import it.epicode.W6_D5_test_Gestione_Eventi2.exceptions.EventoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class EventoService {

    private final EventoRepository eventoRepository;

    private final AppUserRepository appUserRepository;



    //recupero l'utente loggato in sessione così da poter fare controlli sulle request dell'evento
    private AppUser getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
    }

    public Evento creaEvento(@Valid EventoRequest eventoRequest) {
        AppUser loggedInUser = getLoggedInUser();

        // Ignora l'organizerId dalla request e usa l'utente loggato
        Evento evento = new Evento();
        BeanUtils.copyProperties(eventoRequest, evento);
        evento.setOrganizer(loggedInUser);

        return eventoRepository.save(evento);
    }

    public Evento modificaEvento(Long id,@Valid EventoRequest eventoRequest) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        // Verifica che l'utente loggato sia il creatore dell'evento
        AppUser loggedInUser = getLoggedInUser();
        if (!evento.getOrganizer().getId().equals(loggedInUser.getId())) {
            throw new EventoException("Non sei autorizzato a modificare questo evento");
        }

        BeanUtils.copyProperties(eventoRequest, evento);
        evento.setOrganizer(loggedInUser); // Mantiene l'organizzatore originale
        return eventoRepository.save(evento);
    }

    public Evento cancellaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        // Verifica che l'utente loggato sia il creatore dell'evento
        AppUser loggedInUser = getLoggedInUser();
        if (!evento.getOrganizer().getId().equals(loggedInUser.getId())) {
            throw new EventoException("Non sei autorizzato a cancellare questo evento");
        }

        eventoRepository.delete(evento);
        return evento;
    }



}
