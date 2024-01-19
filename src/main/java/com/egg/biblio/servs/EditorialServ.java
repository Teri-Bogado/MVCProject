/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.servs;

import com.egg.biblio.ents.Editorial;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.repositorio.EditorialRepo;
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
public class EditorialServ {

    @Autowired
    EditorialRepo editorialRepo;

    @Transactional
    public void crearEditorial(String nombre) throws MiException {

        validar(nombre);

        Editorial ed = new Editorial();

        ed.setNombre(nombre);

        editorialRepo.save(ed);

    }

    public List<Editorial> listarEditorials() {

        List<Editorial> editoriales = new ArrayList();

        editoriales = editorialRepo.findAll();
        return editoriales;
    }

    public void modificarEditorial(String id, String nombre) throws MiException {

        validar(id, nombre);

        Optional<Editorial> respuestaEditorial = editorialRepo.findById(id);

        if (respuestaEditorial.isPresent()) {

            Editorial edi = respuestaEditorial.get();
            edi.setNombre(nombre);
            editorialRepo.save(edi);

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
    
    public Editorial getOne(String id) {
        return editorialRepo.getOne(id);
    }
}
