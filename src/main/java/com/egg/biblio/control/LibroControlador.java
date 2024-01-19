/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.biblio.control;

import com.egg.biblio.ents.Autor;
import com.egg.biblio.ents.Editorial;
import com.egg.biblio.ents.Libro;
import com.egg.biblio.exceptions.MiException;
import com.egg.biblio.servs.AutorServ;
import com.egg.biblio.servs.EditorialServ;
import com.egg.biblio.servs.LibroServ;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author "J"
 */
@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServ LS;
    @Autowired
    private AutorServ AS;
    @Autowired
    private EditorialServ ES;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {

        List<Autor> autores = AS.listarAutores();
        List<Editorial> editoriales = ES.listarEditorials();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);

        return "libro_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")

    public String registro(@RequestParam(required = false) Long ISBN, @RequestParam String titulo,
            @RequestParam(required = false) Integer ejemplares, @RequestParam String idAutor,
            @RequestParam String idEditorial, ModelMap modelo) {

        try {
            LS.crearLibro(ISBN, titulo, ejemplares, idAutor, idEditorial);

            modelo.put("exito", "El libro fue cargado con éxito");

        } catch (MiException ex) {
            List<Autor> autores = AS.listarAutores();
            List<Editorial> editoriales = ES.listarEditorials();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", ex.getMessage());
            return "libro_form.html";
        }

        return "index.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Libro> libros = LS.listarLibros();

        modelo.addAttribute("libros", libros);

        return "libro_list.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{ISBN}")
    public String modificar(@PathVariable Long ISBN, ModelMap modelo) {
        
        List<Autor> autores = AS.listarAutores();
        List<Editorial> editoriales = ES.listarEditorials();
        
        modelo.addAttribute("libro", LS.getOne(ISBN));
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_modificar.html";
        
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{ISBN}")
    public String modificar(@PathVariable Long ISBN, @RequestParam String titulo, @RequestParam String idAutor, @RequestParam String idEditorial, Integer ejemplares, ModelMap modelo ) {
        
        try {
            
            
        
        LS.modificarLibro(ISBN, titulo, ejemplares, idAutor, idEditorial);
        modelo.put("exito", "El libro se ha modificado con éxito.");
        return "redirect:../lista";
        } catch(MiException ex) {
            
            modelo.put("error", ex.getMessage());
            return "libro_modificar.html";
            
        }
        
    }
}
