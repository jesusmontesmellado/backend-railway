package proyectoJMM.penyas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectoJMM.penyas.modelo.Jugador;

import java.util.List;


//interface JpaRepository es del proyecto de spring y le pasamos el tipo de clase
//de entidad que le vamos a pasar y tipo de la PK
// //para tener los metodos para comunicarnos con la BD (lo simplifica Spring)
public interface JugadorRepositorio extends JpaRepository<Jugador,Integer> {

    List<Jugador> findByPenyaIdPenya(Integer idPenya);
}
