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

public class Equipo {
    @Id  //Clave primaria
    //valor clave primaria
    // Indica que el valor de la clave primaria será generado automáticamente por la base de datos.
    @GeneratedValue(strategy = GenerationType.IDENTITY)//estrategia de generación será mediante un identificador único gestionado por la BD*/
    Integer idEquipo;

    String nombreEquipo;

    @ManyToOne
    @JoinColumn(name="idPenya")
    Penya penya;

}