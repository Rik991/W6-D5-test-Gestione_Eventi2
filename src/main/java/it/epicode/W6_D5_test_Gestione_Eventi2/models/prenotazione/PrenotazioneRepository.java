package it.epicode.W6_D5_test_Gestione_Eventi2.models.prenotazione;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByUserId(Long userId);
}
