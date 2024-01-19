/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.control;

import com.egg.biblio.ents.Usuario;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.servs.UsuarioServ;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author "J"
 */
@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    UsuarioServ US;
    
    @GetMapping("/")
    public String index() {
        
        return "index.html";
    }
    
    @GetMapping("/registrar")
    public String registrar() {
        
        return "registro.html";
    }
    
    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2,
            ModelMap modelo) {
        
        
        try {
            US.crearUsuario(archivo, nombre, email, password, password2);
            modelo.put("exito", "El usuario se ha registrado.");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "registro.html";
        }
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        
        if(error != null){ 
            modelo.put("error", "Usuario o contraseña inválidos.");
            
        }        
        return "login.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario loggedUser = (Usuario) session.getAttribute("usuariosession");
        
        if(loggedUser.getRol().toString().equals("ADMIN")) {
            
            return "redirect:/admin/dashboard";
            
        }
        
        return "inicio.html";
    }
   
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        
        modelo.put("usuario", usuario);
        
        return "usuario_modificar.html";
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizarPerfil(MultipartFile archivo, @PathVariable String id,
            @RequestParam String email, @RequestParam String nombre, @RequestParam String password,
            @RequestParam String password2,
            ModelMap modelo) {
        
        try {
            US.actualizarUsuario(archivo, nombre, email, password, password2, id);
            modelo.put("exito", "Se han actualizado los datos con éxito.");
            return "inicio.html";
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
        
        return "usuario_modificar.html";
        }
        
        
        
    }
    
}
