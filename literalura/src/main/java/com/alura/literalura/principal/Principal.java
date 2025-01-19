package com.alura.literalura.principal;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alura.literalura.dto.AutorDTO;
import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.dto.RespuestaLibrosDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;

@Component
public class Principal {
	
	@Autowired
	private LibroService libroService;
	
	@Autowired
	private AutorService autorService;
	
	@Autowired
	private ConsumoAPI consumoAPI;
	
	@Autowired
	private ConvierteDatos convierteDatos;
	
	private static final String base_url = "https://gutendex.com/books/";
	
	public void mostrarMenu() {
		Scanner sc = new Scanner(System.in);
		int opcion;
		do {
			System.out.println("Bienvenido a ---- LITERALURA ----");
			System.out.println("1. Buscar libros por título");
			System.out.println("2. Listar libros registrados");
			System.out.println("3. Listar autores registrados");
			System.out.println("4. Listar autores vivos en cierto año");
			System.out.println("5. Listar libros por idioma");
			System.out.println("0. Salir");
			System.out.println("Seleccione una opción:");
			opcion = sc.nextInt();
			sc.nextLine();

			switch (opcion) {
			// case 1: buscar libros por título
			case 1:
				System.out.println("Ingrese el título del libro:");
				String titulo = sc.nextLine();
				try {
					String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
					String json = consumoAPI.obtenerDatos(base_url + "?search=" + encodedTitulo);
					RespuestaLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, RespuestaLibrosDTO.class);
					List<LibroDTO> librosDTO = respuestaLibrosDTO.getLibros();
					if (librosDTO.isEmpty()) {
						System.out.println("No se encontraron libros con el título ingresado");
					} else {
						boolean libroRegistrado = false;
						for (LibroDTO libroDTO : librosDTO) {
							if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
								Optional<Libro> libroExistente = libroService.buscarLibroPorTitulo(titulo);
								if (libroExistente.isPresent()) {
									System.out.println("El libro ya está registrado");
									System.out.println("¿Desea ver los detalles? (s/n)");
									String respuesta = sc.nextLine();
									if (respuesta.equalsIgnoreCase("s")) {
										System.out.println(libroExistente.get());
									}
									System.out.println("No se puede registrar un libro que ya existe");
									libroRegistrado = true;
									break;
								} else {
									Libro libro = new Libro();
									libro.setTitulo(libroDTO.getTitulo());
									libro.setIdioma(libroDTO.getIdiomas().get(0));
									libro.setNumeroDescargas(libroDTO.getNumeroDescargas());

									// buscar o crear autor
									AutorDTO primerAutorDTO = libroDTO.getAutores().get(0);
									Autor autor = autorService.buscarAutorPorNombre(primerAutorDTO.getNombre())
											.orElseGet(() -> {
												Autor nuevoAutor = new Autor();
												nuevoAutor.setNombre(primerAutorDTO.getNombre());
												nuevoAutor.setAnoNacimiento(primerAutorDTO.getAnoNacimiento());
												nuevoAutor.setAnoFallecimiento(primerAutorDTO.getAnoFallecimiento());
												return autorService.crearAutor(nuevoAutor);
											});
									// asociar autor con libro
									libro.setAutor(autor);
									// guardar libro en la base de datos
									libroService.crearLibro(libro);
									System.out.println("Libro registrado exitosamente " + libro.getTitulo());
									mostrarDetallesLibro(libroDTO);
									libroRegistrado = true;
									break;
								}
							}
						}
						if (!libroRegistrado) {
							System.out.println("No se encontró un libro con el título ingresado");
						}
					}
				} catch (Exception e) {
					System.out.println("Error al buscar libros: " + e.getMessage());
				}
				break;
			// case 2: listar libros registrados
			case 2:
				libroService.listarLibros().forEach(libro -> {
					System.out.println("-------LIBRO-------");
					System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));					
					System.out.println("Idioma: " + libro.getIdioma());
					System.out.println("Número de descargas: " + libro.getNumeroDescargas());
				});
				break;
			// case 3: listar autores registrados
			case 3:
				autorService.listarAutores().forEach(autor -> {
					System.out.println("-------AUTOR-------");
					System.out.println("Nombre: " + autor.getNombre());
					System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
					System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
					String libros = autor.getLibros().stream().map(Libro::getTitulo).collect(Collectors.joining(", "));
					System.out.println("Libros: [ " + libros + " ] ");
				});
				break;
			// case 4: listar autores vivos en cierto año
			case 4:
				System.out.println("Ingrese el año en el que el(los) autor(es) que desea buscar estaba(n) vivo(s) :");
				int ano = sc.nextInt();
				sc.nextLine();
				List<Autor> autoresVivos = autorService.listarAutoresVivosEnAno(ano);
				if (autoresVivos.isEmpty()) {
					System.out.println("No se encontraron autores vivos en el año ingresado");
				} else {
					autoresVivos.forEach(autor -> {
						System.out.println("-------AUTOR-------");
						System.out.println("Nombre: " + autor.getNombre());
						System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
						System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
						System.out.println("Cantidad de libros escritos: " + autor.getLibros().size());
					});
				}
				break;
			// case 5: listar libros por idioma
			case 5:
				System.out.println("Ingrese el idioma del libro que desea buscar:");
				System.out.println("es");
				System.out.println("en");
				System.out.println("fr");
				System.out.println("pt");
				String idioma = sc.nextLine();
				if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma)
						|| "pt".equalsIgnoreCase(idioma)) {
					libroService.listarLibrosPorIdioma(idioma).forEach(libro -> {
						System.out.println("-------LIBRO-------");
						System.out.println("Título: " + libro.getTitulo());
	                    System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "Desconocido"));					
						System.out.println("Idioma: " + libro.getIdioma());
						System.out.println("Número de descargas: " + libro.getNumeroDescargas());
					});
				} else {
					System.out.println("Idioma no válido");
				}
				break;
			// case 0: salir
			case 0:
				System.out.println("Hasta luego");
				break;
			default: 
				System.out.println("Opción no válida, intente de nuevo");
			}
		} while (opcion != 0);
		sc.close();
	}

	private void mostrarDetallesLibro(LibroDTO libroDTO) {
		System.out.println("-------DETALLES DEL LIBRO-------");
		System.out.println("Título: " + libroDTO.getTitulo());
		System.out.println("Autores: ");
		libroDTO.getAutores().forEach(autor -> {
			System.out.println("Nombre: " + autor.getNombre());
			System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
			System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
		});
		System.out.println("Idiomas: " + libroDTO.getIdiomas());
		System.out.println("Número de descargas: " + libroDTO.getNumeroDescargas());
	}
}
