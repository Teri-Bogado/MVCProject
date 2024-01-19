/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.servs;

import com.egg.biblio.ents.Imagen;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.repositorio.ImagenRepo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author "J"
 */
@Service
public class ImagenServ {

    @Autowired
    private ImagenRepo IR;

    public Imagen guardar(MultipartFile archivo) throws MiException {
        if (archivo != null) {

            try {

                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());

                imagen.setNombre(archivo.getName());

                imagen.setContenido(archivo.getBytes());

                return IR.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());
                return null;
            }

        } else {
            return null;
        }

    }

    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiException {

        if (archivo != null) {

            try {

                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> respuesta = IR.findById(idImagen);

                    if (respuesta.isPresent()) {
                        
                        imagen = respuesta.get();
                    }
                }
                        
                        imagen.setMime(archivo.getContentType());

                        imagen.setNombre(archivo.getName());

                        imagen.setContenido(archivo.getBytes());
                    

                    return IR.save(imagen);
                
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return null;
            }
        } else {
            return null;
        }

    }

}
