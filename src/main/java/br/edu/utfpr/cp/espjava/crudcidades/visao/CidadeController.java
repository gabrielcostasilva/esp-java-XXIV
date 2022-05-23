package br.edu.utfpr.cp.espjava.crudcidades.visao;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CidadeController {

    private Set<Cidade> cidades;

    public CidadeController() {
        cidades = new HashSet<>();
    }

    @GetMapping("/")
    public String listar(Model memoria) {

        memoria.addAttribute("listaCidades", cidades);

        return "/crud";
    }

    @PostMapping("/criar")
    public String criar(@Valid Cidade cidade, BindingResult validacao) {

        if (validacao.hasErrors()) {
            validacao
                .getFieldErrors()
                .forEach(error -> 
                        System.out.println(
                            String.format("O atributo %s emitiu a seguinte mensagem %s", 
                                error.getField(),
                                error.getDefaultMessage()
                            )
                        )
                );
        } else {
            cidades.add(cidade);
        }

        return "redirect:/";
    }

    @GetMapping("/excluir")
    public String excluir(
            @RequestParam String nome, 
            @RequestParam String estado) {

        cidades.removeIf(cidadeAtual -> 
                    cidadeAtual.getNome().equals(nome) && 
                    cidadeAtual.getEstado().equals(estado));

        return "redirect:/";
    }

    @GetMapping("/preparaAlterar")
    public String preparaAlterar(
        @RequestParam String nome, 
        @RequestParam String estado,
        Model memoria) {

            var cidadeAtual = cidades
                    .stream()
                    .filter(cidade -> 
                        cidade.getNome().equals(nome) && 
                        cidade.getEstado().equals(estado))
                    .findAny();

            if (cidadeAtual.isPresent()) {
                memoria.addAttribute("cidadeAtual", cidadeAtual.get());
                memoria.addAttribute("listaCidades", cidades);
            }

            return "/crud";
    }

    @PostMapping("/alterar")
    public String alterar(
        @RequestParam String nomeAtual, 
        @RequestParam String estadoAtual,
        Cidade cidade) {

            cidades.removeIf(cidadeAtual -> 
                    cidadeAtual.getNome().equals(nomeAtual) && 
                    cidadeAtual.getEstado().equals(estadoAtual));

            criar(cidade, null);

            return "redirect:/";
    }
}

