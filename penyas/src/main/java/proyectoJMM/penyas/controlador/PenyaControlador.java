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

public class PenyaControlador {
//atributo para enviar informacion por consola usando el logger de nuestra aplicacion (Logger sflj)
    private static final Logger logger= LoggerFactory.getLogger(PenyaControlador.class);

    //para poder comunicarnos entre las distintas hacemos una inyeccion de dependencia @AutoWire
    //desde el servicio hasta el Controlador
    @Autowired
    private PenyaServicio penyaServicio;
    @Autowired
    private JugadorServicio jugadorServicio;
    @Autowired
    private EquipoServicio equipoServicio;



    //http://localhost:8080/penyas-app/v1/penyas
    @GetMapping ("/penyas")
    public List<Penya> obtenerPenyas(){
        List<Penya> penyas = this.penyaServicio.listarPenyas();
        logger.info("penyas obtenidas:");
        penyas.forEach((penya ->logger.info(penya.toString()) ));
        return penyas;
    }
    //peticion de tipo post (misma url)
    @PostMapping("/penyas")
    //como recibimos la info desde un formulario de Angular, esta informacion va estar dentro de
    //la peticion post, por ello usamos @RequestBody, ya que la informacion que vamos a recibir se
    // encuentra dentro del cuerpo de esta peticion. Y lo que recibimos es un nuevo objeto de tipo Penya

    public Penya agregarPenya(@RequestBody Penya penya){
        logger.info("Penya a agregar: "+ penya);
        this.penyaServicio.guardarPenya(penya);
        this.crearEquipos(penya,penya.getEquipo1(),penya.getEquipo2());
        return penya;

    }

    @GetMapping("/penyas/{id}") //solicitamos un id del objeto a buscar en la BD
    //devuelve un objeto Penya, pero envuelto en un objeto de tipo ResponseEntity, esto es parte
    //del API de Rest de Spring que nos permite regresar informacion de manera muy sencilla
    public ResponseEntity<Penya> obtenerPenyaPorId(
            @PathVariable int id){
        Penya penya = this.penyaServicio.buscarPenyaPorId(id);
        if(penya !=null) {
            return ResponseEntity.ok(penya);
        }
        else
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+ id);
    }

    @PutMapping("/penyas/{id}")
    public ResponseEntity<Penya> actualizarPenya(
            @PathVariable int id,
            @RequestBody Penya penyaRecibida){
        //para ver si existe la penya en la BD
        Penya penya=this.penyaServicio.buscarPenyaPorId(id);
        if(penya==null){
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+id);
        }
        penya.setNombrePenya(penyaRecibida.getNombrePenya());
        penya.setCuota(penyaRecibida.getCuota());
        penya.setPuntosDerrota(penyaRecibida.getPuntosDerrota());
        penya.setPuntosEmpate(penyaRecibida.getPuntosEmpate());
        penya.setPuntosVictoria(penyaRecibida.getPuntosVictoria());
        penya.setSorteo(penyaRecibida.getSorteo());
        penya.setConsiderarIncompatibilidades(penyaRecibida.getConsiderarIncompatibilidades());
        penya.setEquilibrarNiveles(penyaRecibida.getEquilibrarNiveles());
        penya.setEquilibrarPosiciones(penyaRecibida.getEquilibrarPosiciones());
        penya.setEquipo1(penyaRecibida.getEquipo1());
        penya.setEquipo2(penyaRecibida.getEquipo2());
        this.editarEquipos(penya,penya.getEquipo1(),penya.getEquipo2());

        this.penyaServicio.guardarPenya(penya);
        return  ResponseEntity.ok(penya);
    }

    @DeleteMapping ("/penyas/{id}")
    //regresa objeto de tipo ResponseEntity, pero en este caso con una cadena
    //de "eliminado" y una respuesta de tipo boolean, para que en momento dado el cliente
    //tambien pueda hacer algo con esta respuesta
    public ResponseEntity<Map<String, Boolean>>
    eliminarPenya(@PathVariable int id){
      Penya penya = penyaServicio.buscarPenyaPorId(id);
      if(penya==null) {
          throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+id);
      }
            //borramos jugadores asociados
            List<Jugador> jugadores = this.jugadorServicio.obtenerJugadoresPorPenya(penya.getIdPenya());
            this.jugadorServicio.borrarJugadores(jugadores);
            //borramos equipos asociados
             List<Equipo> equipos = this.equipoServicio.obtenerEquiposPorPenya(penya.getIdPenya());
            this.equipoServicio.borrarEquipos(equipos);


             this.penyaServicio.borrarPenyaPorId(penya.getIdPenya());
            //procesamos la respuesta, los dos valores
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("eliminado", Boolean.TRUE);
            return ResponseEntity.ok(respuesta);


    }
    //TODO cambios en como almacenar equipos
    public void crearEquipos(Penya penya, String nombreEquipo1, String nombreEquipo2){

        String nombreEquipoNoConvocados = "No Convocados";
        Equipo equipo0 = new Equipo();
        equipo0.setNombreEquipo(nombreEquipoNoConvocados);
        equipo0.setPenya(penya);
        Equipo equipo1 = new Equipo();
        equipo1.setNombreEquipo(nombreEquipo1);
        equipo1.setPenya(penya);
        Equipo equipo2 = new Equipo();
        equipo2.setNombreEquipo(nombreEquipo2);
        equipo2.setPenya(penya);
        this.equipoServicio.guardarEquipo(equipo0);
        this.equipoServicio.guardarEquipo(equipo1);
        this.equipoServicio.guardarEquipo(equipo2);
    }

    public void editarEquipos(Penya penya, String nombreEquipo1, String nombreEquipo2){
        List<Equipo> equipos = this.equipoServicio.obtenerEquiposPorPenya(penya.getIdPenya());
            // Asegúrate de que haya al menos 3 equipos
            if (equipos.size() >= 3) {
                // No modificamos el equipo en la posición 0, dejamos su nombre igual

                // Modificamos el nombre del equipo en la posición 1
                equipos.get(1).setNombreEquipo(nombreEquipo1);

                // Modificamos el nombre del equipo en la posición 2
                equipos.get(2).setNombreEquipo(nombreEquipo2);

                // Guardamos los equipos modificados
                equipos.forEach(equipo -> this.equipoServicio.guardarEquipo(equipo));
            } else {
                logger.info("No hay suficientes equipos, tamaño: "+ equipos.size());
                // Manejo de error: No hay suficientes equipos
                // Puedes lanzar una excepción, enviar un mensaje de error, etc.
                // ...
            }
        }
}
