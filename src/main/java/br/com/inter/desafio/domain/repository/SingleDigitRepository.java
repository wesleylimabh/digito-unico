package br.com.inter.desafio.domain.repository;

import br.com.inter.desafio.domain.model.SingleDigit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleDigitRepository extends JpaRepository<SingleDigit, Long> {

}
