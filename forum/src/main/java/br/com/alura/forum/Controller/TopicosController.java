package br.com.alura.forum.Controller;

import br.com.alura.forum.Form.AtualizcaoTopicoForm;
import br.com.alura.forum.Repositor.CursoRepsitory;
import br.com.alura.forum.Controller.Dto.DetalesDoTopicoDto;
import br.com.alura.forum.Controller.Dto.TopicoDto;
import br.com.alura.forum.Form.TopicoForm;
import br.com.alura.forum.Repositor.TopicoRepository;
import br.com.alura.forum.modelo.Topico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topico")
public class TopicosController {
    @Autowired
   private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepsitory cursoRepository;
    @GetMapping
    public List<TopicoDto> list(String nomeCurso){
        if (nomeCurso == null){
            List<Topico> topicos = topicoRepository.findAll();
            return  TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return  TopicoDto.converter(topicos);
        }

    }
    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
        Topico topico = form.conveter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));

    }
    @GetMapping("/{id}")
    public ResponseEntity<DetalesDoTopicoDto> detalhar(@PathVariable Long id){
     Optional <Topico> topico = topicoRepository.findById(id);
     if (topico.isPresent()){
         return ResponseEntity.ok(new DetalesDoTopicoDto(topico.get()));
     }
     return ResponseEntity.notFound().build();

    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizcaoTopicoForm form){
        Optional <Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()){
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));

        }

        return ResponseEntity.notFound().build();

    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id){
        Optional <Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()){
             topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}