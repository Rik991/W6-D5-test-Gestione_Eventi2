package it.epicode.W6_D5_test_Gestione_Eventi2.models.evento;


import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUser;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.AppUserRepository;
import it.epicode.W6_D5_test_Gestione_Eventi2.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    private final AppUserRepository appUserRepository;


    public Evento creaEvento(EventoRequest eventoRequest) {
        AppUser organizer = appUserRepository.findById(eventoRequest.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizzatore non trovato"));
        Evento evento = new Evento();
        BeanUtils.copyProperties(eventoRequest, evento);
        evento.setOrganizer(organizer);
        return eventoRepository.save(evento);
    }

    public Evento modificaEvento(Long id, EventoRequest eventoRequest) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        BeanUtils.copyProperties(eventoRequest, evento);
        return eventoRepository.save(evento);
    }

    public Evento cancellaEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));
        eventoRepository.delete(evento);
        return evento;
    }



}
