package proyectoJMM.penyas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoJMM.penyas.modelo.Equipo;
import proyectoJMM.penyas.repositorio.EquipoRepositorio;

import java.util.List;

//con esto ya podemos usar la capa de repositorio
@Service
public class EquipoServicio implements IEquipoServicio{
    @Autowired
    private EquipoRepositorio equipoRepositorio;

    public List<Equipo> obtenerEquiposPorPenya(Integer idPenya) {
        return this.equipoRepositorio.findByPenyaIdPenya(idPenya);
    }
    @Override
    public List<Equipo> listarEquipos() {
        return this.equipoRepositorio.findAll();
    }

    @Override
    public Equipo buscarEquipoPorId(Integer idEquipo) {
        Equipo equipo=this.equipoRepositorio.findById(idEquipo).orElse(null);

        return equipo;
    }

    @Override
    public Equipo guardarEquipo(Equipo equipo) {
       return this.equipoRepositorio.save(equipo); //si el idEquipo(PK) es nulo->insert sino->update
    }

    @Override
    public void borrarEquipoPorId(Integer idEquipo) {
        this.equipoRepositorio.deleteById(idEquipo);
    }
    public void borrarEquipos(List<Equipo> equipos) {
        for (Equipo equipo : equipos) {
            equipoRepositorio.deleteById(equipo.getIdEquipo());
        }
    }
}

