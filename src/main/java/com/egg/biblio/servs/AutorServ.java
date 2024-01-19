/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.servs;

import com.egg.biblio.ents.Autor;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.repositorio.AutorRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author "J"
 */
@Service
public class AutorServ {

    @Autowired
    private AutorRepo autorRepo;

    @Transactional
    public void crearAutor(String nombre) throws MiException {

        validar(nombre);
        
        Autor aut = new Autor();
        aut.setNombre(nombre);

        autorRepo.save(aut);

    }

    public List<Autor> listarAutores() {

        List<Autor> autores = new ArrayList();

        autores = autorRepo.findAll();
        return autores;
    }

    public void modificarAutor(String id, String nombre) throws MiException{
        validar(id, nombre);
        
        Optional<Autor> resAutor = autorRepo.findById(id);

        if (resAutor.isPresent()) {
            Autor aut = resAutor.get();

            aut.setNombre(nombre);

            autorRepo.save(aut);

        } else {
            throw new MiException("No existe se autor.");
        }

    }
    
    private void validar(String id, String nombre) throws MiException {
        
        if (id == null || id.isEmpty()) {
            throw new MiException("El id no puede estar vacío");
        }
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede estar vacío");
        }
        
        
    }
    
    private void validar(String nombre) throws MiException {
        
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede estar vacío");
        }
        
        
    }
    
    public Autor getOne(String id) {
        
        return autorRepo.getOne(id);
        
    }

}
