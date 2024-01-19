/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.servs;

import com.egg.biblio.ents.Imagen;
import com.egg.biblio.ents.Usuario;
import com.egg.biblio.enums.Rol;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.repositorio.UsuarioRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author "J"
 */
@Service
public class UsuarioServ implements UserDetailsService {

    @Autowired
    private UsuarioRepo userRepo;

    @Autowired
    private ImagenServ IS;
    
    
    public Usuario getOne(String id) {
        
        return userRepo.getOne(id);
        
    }
    
    @Transactional
    public void crearUsuario(MultipartFile archivo, String nombre,
            String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        usuario.setRol(Rol.USER);

        Imagen img = IS.guardar(archivo);

        usuario.setImagen(img);

        userRepo.save(usuario);

    }

    @Transactional
    public void actualizarUsuario(MultipartFile archivo, String nombre, String email,
            String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = userRepo.findById(email);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);

            String idImagen = null;
            
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            Imagen img = IS.actualizar(archivo, idImagen);
            
            usuario.setImagen(img);

            userRepo.save(usuario);

        }

    }

    @Transactional
    public void actualizarUsuario(MultipartFile archivo, String nombre, String email,
            String password, String password2, String id) throws MiException {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = userRepo.findById(id);

        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);

            String idImagen = null;
            
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }
            
            Imagen img = IS.actualizar(archivo, idImagen);
            
            usuario.setImagen(img);

            userRepo.save(usuario);

        }

    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepo.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacío");
        }

        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacío");
        }

        if (password.isEmpty() || password == null) {
            throw new MiException("El password no puede estar vacío");
        }

        if (password.length() < 6) {
            throw new MiException("El password debe ser de 6 caracteres o más.");
        }

        if (password2.isEmpty() || password2 == null) {
            throw new MiException("Ingrese confirmación de password");
        }

        if (!password.equals(password2)) {
            throw new MiException("Los campos de password deben coincidir.");
        }

    }

}
