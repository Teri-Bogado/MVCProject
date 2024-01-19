/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.servs;

import com.egg.biblio.ents.Autor;
import com.egg.biblio.ents.Editorial;
import com.egg.biblio.ents.Libro;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.repositorio.AutorRepo;
import com.egg.biblio.repositorio.EditorialRepo;
import com.egg.biblio.repositorio.LibroRepo;
import java.util.ArrayList;
import java.util.Date;
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
public class LibroServ {
    //crear, editar, habilitado/deshabilitado

    @Autowired
    private LibroRepo libroRepo;

    @Autowired
    private AutorRepo autorRepo;

    @Autowired
    private EditorialRepo editorialRepo;

    @Transactional
    //si el metodo no lanza excepciones se hace el commit en la DB, 
    //si hay excepcion y no se atrapa, se hace rollback
    public void crearLibro(Long ISBN, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(ISBN, titulo, ejemplares, idAutor, idEditorial);

        Optional<Libro> respuesta = libroRepo.findById(ISBN);
        if (respuesta.isPresent()) {
            throw new MiException("Ya existe un libro con ese ISBN");
        } else {

            Autor autor = autorRepo.findById(idAutor).get();
            Editorial editorial = editorialRepo.findById(idEditorial).get();

            Libro lib = new Libro();

            lib.setISBN(ISBN);
            lib.setTitulo(titulo);
            lib.setEjemplares(ejemplares);

            lib.setAlta(new Date());

            lib.setAutor(autor);
            lib.setEditorial(editorial);

            libroRepo.save(lib);
        }

    }

    public List<Libro> listarLibros() {

        List<Libro> libros = new ArrayList();

        libros = libroRepo.findAll();
        return libros;
    }

    public void modificarLibro(Long ISBN, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {

        validar(ISBN, titulo, ejemplares, idAutor, idEditorial);

        Autor aut = new Autor();
        Editorial edi = new Editorial();
        Optional<Libro> respuesta = libroRepo.findById(ISBN);
        Optional<Autor> resAutor = autorRepo.findById(idAutor);
        Optional<Editorial> resEditorial = editorialRepo.findById(idEditorial);

        if (resAutor.isPresent()) {

            aut = resAutor.get();

        }

        if (resEditorial.isPresent()) {

            edi = resEditorial.get();

        }

        if (respuesta.isPresent()) {

            Libro lib = respuesta.get();

            if (resAutor.isPresent()) {
                lib.setTitulo(titulo);
                lib.setAutor(aut);
                lib.setEditorial(edi);
                lib.setEjemplares(ejemplares);
                libroRepo.save(lib);
            }

        }

    }

    private void validar(Long ISBN, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiException {
        if (ISBN == null) {
            throw new MiException("El ISBN no puede ser nulo");
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new MiException("El título no puede ser nulo o estar vacío");
        }

        if (idAutor == null || idAutor.isEmpty()) {
            throw new MiException("El id del autor no puede ser nulo");
        }

        if (idEditorial == null || idEditorial.isEmpty()) {
            throw new MiException("El id de la editorial no puede ser nulo");
        }

        if (ejemplares == null) {
            throw new MiException("Los ejemplares no pueden ser nulos");
        }
    }

    public Libro getOne(Long ISBN) {

        return libroRepo.getOne(ISBN);

    }
}
