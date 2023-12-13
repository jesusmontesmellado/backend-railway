package proyectoJMM.penyas.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyectoJMM.penyas.excepcion.RecursoNoEncontradoExcepcion;
import proyectoJMM.penyas.modelo.Equipo;
import proyectoJMM.penyas.modelo.Jugador;
import proyectoJMM.penyas.modelo.Penya;
import proyectoJMM.penyas.servicio.EquipoServicio;
import proyectoJMM.penyas.servicio.JugadorServicio;
import proyectoJMM.penyas.servicio.PenyaServicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//creamos un "context-path para que las peticiones no esten en localhost:8080, sino en un nombre
//localhost:8080/penyas-app
@RequestMapping("penyas-app/v1")

//para hacer peticiones desde el front-end de angular
@CrossOrigin (value="http://localhost:4200")

public class JugadorControlador {
//atributo para enviar informacion por consola usando el logger de nuestra aplicacion (Logger sflj)
    private static final Logger logger= LoggerFactory.getLogger(JugadorControlador.class);

    //para poder comunicarnos entre las distintas hacemos una inyeccion de dependencia @AutoWire
    //desde el servicio hasta el Controlador
    @Autowired
    private JugadorServicio jugadorServicio;
    @Autowired
    private PenyaServicio penyaServicio;

    @Autowired
    private EquipoServicio equipoServicio;

    //http://localhost:8080/penyas-app/v1/jugadores

    @GetMapping ("/jugadores/{idPenya}")
    public List<Jugador> obtenerJugadoresPorIdPenya(@PathVariable int idPenya){
        List<Jugador> jugadores = this.jugadorServicio.obtenerJugadoresPorPenya(idPenya);
        logger.info("Jugadores obtenidos para la penya con id: {}", idPenya);
        jugadores.forEach((jugador ->logger.info(jugador.toString()) ));
        return jugadores;
    }
    //peticion de tipo post (misma url)

    //TODO corregir para que guarde en la penya correspondiente

    //como recibimos la info desde un formulario de Angular, esta informacion va estar dentro de
    //la peticion post, por ello usamos @RequestBody, ya que la informacion que vamos a recibir se
    // encuentra dentro del cuerpo de esta peticion. Y lo que recibimos es un nuevo objeto de tipo Jugador
    //TODO comprobar que funciona

    @PostMapping("/jugadores/crearJugador/{id}")
    public Jugador agregarJugador(@PathVariable int id, @RequestBody Jugador jugador) {
        // Buscar la penya por id
        Penya penya = this.penyaServicio.buscarPenyaPorId(id);

        // Asignar la penya al jugador
        jugador.setPenya(penya);

        // Crear o buscar el equipo 0 (no convocados por defecto)
        Equipo equipo0 = this.equipoServicio.obtenerEquiposPorPenya(jugador.getPenya().getIdPenya()).get(0);

        if (equipo0 == null) {
            equipo0 = new Equipo();
            equipo0.setIdEquipo(1);
            equipo0.setNombreEquipo("equipo0");
            this.equipoServicio.guardarEquipo(equipo0);
        }

        // Asignar el equipo al jugador
        jugador.setEquipo(equipo0);

        // Guardar el jugador en la base de datos
        logger.info("Jugador a agregar: " + jugador);
        return this.jugadorServicio.guardarJugador(jugador);
    }
    @PostMapping("/jugadores/editarJugador/{id}")
    public Jugador editarJugador(@PathVariable int id,@RequestBody Jugador jugador){
        Penya penya = this.penyaServicio.buscarPenyaPorId(id);
        jugador.setPenya(penya);
        logger.info("Jugador a editar: "+ jugador);
        return this.jugadorServicio.guardarJugador(jugador);

    }
    @GetMapping("/jugadores/jugador/{id}") //solicitamos un id del objeto a buscar en la BD
    //devuelve un objeto Penya, pero envuelto en un objeto de tipo ResponseEntity, esto es parte
    //del API de Rest de Spring que nos permite regresar informacion de manera muy sencilla
    public ResponseEntity<Jugador> obtenerJugadorPorId(
            @PathVariable int id){
        Jugador jugador = this.jugadorServicio.buscarJugadorPorId(id);
        if(jugador !=null) {
            return ResponseEntity.ok(jugador);
        }
        else
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+ id);
    }
    @DeleteMapping ("/jugadores/borrarJugador/{id}")
    //regresa objeto de tipo ResponseEntity, pero en este caso con una cadena
    //de "eliminado" y una respuesta de tipo boolean, para que en momento dado el cliente
    //tambien pueda hacer algo con esta respuesta
    public ResponseEntity<Map<String, Boolean>>
    eliminarJugador(@PathVariable int id){
        Jugador jugador = jugadorServicio.buscarJugadorPorId(id);
        if(jugador==null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+id);
        }
        this.jugadorServicio.borrarJugadorPorId(id);
        //procesamos la respuesta, los dos valores
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);


    }
}
