package br.edu.utfpr.cp.espjava.crudcidades.cidade;

import java.security.Principal;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CidadeController {

    private final CidadeRepository repository;

    public CidadeController(CidadeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String listar(Model memoria, Principal usuario, HttpSession sessao) {

        memoria.addAttribute("listaCidades", repository
                                                    .findAll()
                                                    .stream()
                                                    .map(Cidade::clonar)
                                                    .collect(Collectors.toList()));

        sessao.setAttribute("usuarioAtual", usuario.getName());
                                                    
        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao, Model memoria) {

        if (validacao.hasErrors()) {
            validacao
                .getFieldErrors()
                .forEach(error -> 
                        memoria.addAttribute(
                            error.getField(), 
                            error.getDefaultMessage())
                        );

                memoria.addAttribute("nomeInformado", cidade.getNome());
                memoria.addAttribute("estadoInformado", cidade.getEstado());
                memoria.addAttribute("listaCidades", repository.findAll());
                
                return ("/crud");
        } else {
            repository.save(cidade.clonar());
        }

        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome, 
            @RequestParam String estado) {
        
        var cidadeEstadoEncontrada = repository.findByNomeAndEstado(nome, estado);

        cidadeEstadoEncontrada.ifPresent(repository::delete);

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome, 
        @RequestParam String estado,
        Model memoria) {

            var cidadeAtual = repository.findByNomeAndEstado(nome, estado);

            cidadeAtual.ifPresent(cidadeEncontrada -> {
                memoria.addAttribute("cidadeAtual", cidadeEncontrada);
                memoria.addAttribute("listaCidades", repository.findAll());
            });

            return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual, 
        @RequestParam String estadoAtual,
        Cidade cidade) {

            var cidadeAtual = repository.findByNomeAndEstado(nomeAtual, estadoAtual);

            if (cidadeAtual.isPresent()) {
                
                var cidadeEncontrada = cidadeAtual.get();
                cidadeEncontrada.setNome(cidade.getNome());
                cidadeEncontrada.setEstado(cidade.getEstado());

                repository.saveAndFlush(cidadeEncontrada);
            }

            return "redirect:/";
    }
}

