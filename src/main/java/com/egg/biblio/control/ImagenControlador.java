/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.control;

import com.egg.biblio.ents.Usuario;
import com.egg.biblio.servs.UsuarioServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author "J"
 */

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServ US;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {
        
        Usuario usuario = US.getOne(id);
        
        byte[] imagen = usuario.getImagen().getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);      
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        
        
    }
}
