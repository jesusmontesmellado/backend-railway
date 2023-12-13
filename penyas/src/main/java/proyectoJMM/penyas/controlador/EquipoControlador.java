package proyectoJMM.penyas.controlador;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyectoJMM.penyas.excepcion.RecursoNoEncontradoExcepcion;
import proyectoJMM.penyas.modelo.Equipo;
import proyectoJMM.penyas.modelo.Penya;
import proyectoJMM.penyas.servicio.EquipoServicio;
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

public class EquipoControlador {
//atributo para enviar informacion por consola usando el logger de nuestra aplicacion (Logger sflj)
    private static final Logger logger= LoggerFactory.getLogger(EquipoControlador.class);

    //para poder comunicarnos entre las distintas hacemos una inyeccion de dependencia @AutoWire
    //desde el servicio hasta el Controlador
    @Autowired
    private EquipoServicio equipoServicio;
    @Autowired
    private PenyaServicio penyaServicio;


    @GetMapping ("/equipos/penya/{idPenya}")
    public List<Equipo> obtenerEquiposPorIdPenya(@PathVariable int idPenya){
        List<Equipo> equipos = this.equipoServicio.obtenerEquiposPorPenya(idPenya);
        logger.info("Equipos obtenidos para la penya con id: {}", idPenya);
        equipos.forEach((equipo ->logger.info(equipo.toString()) ));
        return equipos;
    }
    @GetMapping ("/equipos")
    public List<Equipo> obtenerEquipo(){
        List<Equipo> equipos = this.equipoServicio.listarEquipos();
        logger.info("equipos obtenidos:");
        equipos.forEach((equipo ->logger.info(equipo.toString()) ));
        return equipos;
    }
    //peticion de tipo post (misma url)
    @PostMapping("/equipos/agregarEquipos/{idPenya}")
    //como recibimos la info desde un formulario de Angular, esta informacion va estar dentro de
    //la peticion post, por ello usamos @RequestBody, ya que la informacion que vamos a recibir se
    // encuentra dentro del cuerpo de esta peticion. Y lo que recibimos es un nuevo objeto de tipo Penya

    public Equipo agregarEquipo(@PathVariable int idPenya, @RequestBody Equipo equipo){
        try {
            logger.info("Equipo a agregar: " + equipo);
            Penya penya = this.penyaServicio.buscarPenyaPorId(idPenya);
            equipo.setPenya(penya);

        }
        catch (Exception ex) {
            logger.info("error al crear equipos" + ex);
        }
        return this.equipoServicio.guardarEquipo(equipo);
    }

   @GetMapping("/equipos/{id}") //solicitamos un id del objeto a buscar en la BD
    //devuelve un objeto Penya, pero envuelto en un objeto de tipo ResponseEntity, esto es parte
    //del API de Rest de Spring que nos permite regresar informacion de manera muy sencilla
    public ResponseEntity<Equipo> obtenerEquipoPorId(
            @PathVariable int id){
        Equipo equipo = this.equipoServicio.buscarEquipoPorId(id);
        if(equipo !=null) {
            return ResponseEntity.ok(equipo);
        }
        else
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+ id);
    }

   @PutMapping("/equipos/{id}")
    public ResponseEntity<Equipo> actualizarEquipo(
            @PathVariable int id,
            @RequestBody Equipo equipoRecibido ){
        //para ver si existe el equipo en la BD
        Equipo equipo=this.equipoServicio.buscarEquipoPorId(id);
        if(equipo==null){
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+id);
        }
        equipo.setNombreEquipo(equipoRecibido.getNombreEquipo());
        this.equipoServicio.guardarEquipo(equipo);
        return  ResponseEntity.ok(equipo);
    }

    @DeleteMapping ("/equipos/{id}")
    //regresa objeto de tipo ResponseEntity, pero en este caso con una cadena
    //de "eliminado" y una respuesta de tipo boolean, para que en momento dado el cliente
    //tambien pueda hacer algo con esta respuesta
    public ResponseEntity<Map<String, Boolean>>
    eliminarEquipo(@PathVariable int id){
      Equipo equipo = equipoServicio.buscarEquipoPorId(id);
      if(equipo==null) {
          throw new RecursoNoEncontradoExcepcion("No se encontró el id: "+id);
      }
          this.equipoServicio.borrarEquipoPorId(equipo.getIdEquipo());
          //procesamos la respuesta, los dos valores
          Map<String, Boolean> respuesta = new HashMap<>();
          respuesta.put("eliminado", Boolean.TRUE);
          return ResponseEntity.ok(respuesta);
    }
}
