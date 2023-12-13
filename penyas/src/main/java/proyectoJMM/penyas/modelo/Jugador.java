package proyectoJMM.penyas.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity //nos va a crear nuestra BD o al menos se está mapeando

//el proyecto del lombok nos va a permitir generar todoeste codigo repetitivo en nuestras clases de manera mas simple
@Data //nos va a generar los getter y setter
@NoArgsConstructor //nos crea constructor vacio
@AllArgsConstructor // nos crea constructor con todos los argumentos
@ToString

public class Jugador {
    @Id  //Clave primaria
    /*@GeneratedValue Indica que el valor de la clave primaria será generado automáticamente por la base de datos.
    (strategy = GenerationType.IDENTITY)estrategia de generación será mediante un identificador único gestionado por la BD*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idJugador; //valor clave primaria
    /*id, nombre, nivel, posicion, partidosGanados, partidosPerdidos, partidosEmpatados,
    miembro, ratio, ultimoPartido, jugadorSeleccionado, descripcion, incompatibilidad, email, idPenya, idEquipo)*/
    @Column(nullable = false)
    String nombreJugador;


    //@Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "idPenya", referencedColumnName = "idPenya")
    Penya penya;

    @ManyToOne
    @JoinColumn(name = "idEquipo")
    private Equipo equipo;

    String nivel;

    //TODO restringir valores posibles
    String posicion;

    Boolean miembro=true;

    String descripcion;

    Integer incompatibilidad;

    String email;

    //TODO se debe inicializar a cero?
    Integer partidosGanados;

    Integer partidosEmpatados;

    Integer partidosPerdidos;

    Float ratio;
    //TODO ver si hacerlo boolean o no ponerlo
    String ultimoPartido;

    Boolean jugadorSeleccionado=false;








}
