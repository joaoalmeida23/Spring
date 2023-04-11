package br.com.alura.forum.Repositor;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepsitory extends JpaRepository<Curso, Long> {

    Curso findByNome(String nome);
}
