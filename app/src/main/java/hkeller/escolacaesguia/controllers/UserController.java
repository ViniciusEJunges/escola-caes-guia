package hkeller.escolacaesguia.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import hkeller.escolacaesguia.dtos.AlteracaoSenhaDto;
import hkeller.escolacaesguia.dtos.CreateUserDto;
import hkeller.escolacaesguia.dtos.UsuarioDto;
import hkeller.escolacaesguia.errors.SenhaIncorretaError;
import hkeller.escolacaesguia.models.User;
import hkeller.escolacaesguia.security.SecurityUtil;
import hkeller.escolacaesguia.services.AlterarSenhaUsuarioServico;
import hkeller.escolacaesguia.services.CreateUserRoleService;
import hkeller.escolacaesguia.services.CreateUserService;
import hkeller.escolacaesguia.services.GetAllUsersService;
import hkeller.escolacaesguia.services.ObterUsuarioPorEmailServico;
import hkeller.escolacaesguia.validators.AlteracaoSenhaUsuarioValidator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    CreateUserService createUserService;

    @Autowired
    CreateUserRoleService createUserRoleService;

    @Autowired
    GetAllUsersService getAllUsersService;

    @Autowired
    ObterUsuarioPorEmailServico obterUsuarioPorEmailServico;

    @Autowired
    AlterarSenhaUsuarioServico alterarSenhaUsuarioServico;

    @PostMapping
    public User create(@RequestBody @Valid CreateUserDto createUserDto) {
        var user = new User();
        BeanUtils.copyProperties(createUserDto, user);
        
        return createUserService.execute(user);
    }

    @GetMapping("perfil")
    public String perfil(Model model) {
        String emailUsuarioLogado = SecurityUtil.obterEmailUsuarioLogado();

        UsuarioDto usuarioDto = obterUsuarioPorEmailServico.execute(emailUsuarioLogado);

        model.addAttribute("usuario", usuarioDto);

        return "perfil";
    }

    @GetMapping("alterar-senha")
    public String alterarSenha(Model model) {
        AlteracaoSenhaDto alteracaoSenhaDto = new AlteracaoSenhaDto();

        model.addAttribute("senha", alteracaoSenhaDto);

        return "alterar-senha";
    }

    @PostMapping("alterar-senha")
    public String alterarSenha(
        @Valid @ModelAttribute("senha") AlteracaoSenhaDto alteracaoSenhaDto,
        BindingResult result, 
        Model model) 
    {
        AlteracaoSenhaUsuarioValidator validator = new AlteracaoSenhaUsuarioValidator();
        validator.validate(alteracaoSenhaDto, result);

        if (result.hasErrors()) {
            model.addAttribute("senha", alteracaoSenhaDto);

            return "alterar-senha";
        }

        String emailUsuarioLogado = SecurityUtil.obterEmailUsuarioLogado();

        try {
            alterarSenhaUsuarioServico.execute(emailUsuarioLogado, alteracaoSenhaDto);
        } catch(SenhaIncorretaError err) {
            FieldError fieldError = new FieldError(
                "senha", 
                "senhaAtual", 
                "Senha incorreta");

            result.addError(fieldError);

            return "alterar-senha";
        }

        
        return "redirect:/users/perfil";
    }
}