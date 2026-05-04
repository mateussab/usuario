package com.mateus.usuario.business;

import com.mateus.usuario.business.converter.UsuarioConverter;
import com.mateus.usuario.business.dto.EnderecoDTO;
import com.mateus.usuario.business.dto.TelefoneDTO;
import com.mateus.usuario.business.dto.UsuarioDTO;
import com.mateus.usuario.infrastructure.entity.Endereco;
import com.mateus.usuario.infrastructure.entity.Telefone;
import com.mateus.usuario.infrastructure.entity.Usuario;
import com.mateus.usuario.infrastructure.exceptions.ConflictException;
import com.mateus.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.mateus.usuario.infrastructure.repository.EnderecoRepository;
import com.mateus.usuario.infrastructure.repository.TelefoneRepository;
import com.mateus.usuario.infrastructure.repository.UsuarioRepository;
import com.mateus.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //opcao 1
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
        //opcao 2
        //return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("Email ja cadastrado" + email);
            }
        } catch(ConflictException e ){
            throw new ConflictException("Email ja cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email){
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado" + email)
                            )
            );
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO){
        //Busca o email do usuário através do token (para tirar a obrigatoriedade do email)
        String email = jwtUtil.extractUsername(token.substring(7));
        //Criptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);
        //Busca os dados do usuário no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("Email não localizado"));
        //Mesclou os dados que recebemos na requisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        //Salvou os dados do usuário convertido e depois pegou o retorno e converteu para UsuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco (Long idEndereco, EnderecoDTO enderecoDTO){

        Endereco enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idEndereco));

        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, enderecoEntity);

        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));

    }

    public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO telefoneDTO){

        Telefone telefoneEntity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                new ResourceNotFoundException("Id não encontrado" + idTelefone));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));

    }

}
