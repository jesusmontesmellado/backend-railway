package proyectoJMM.penyas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectoJMM.penyas.modelo.Equipo;


import java.util.List;


//interface JpaRepository es del proyecto de spring y le pasamos el tipo de clase
//de entidad que le vamos a pasar y tipo de la PK
// //para tener los metodos para comunicarnos con la BD (lo simplifica Spring)
public interface EquipoRepositorio extends JpaRepository<Equipo,Integer> {

    List<Equipo> findByPenyaIdPenya(Integer idPenya);

}
