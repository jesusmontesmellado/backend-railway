package proyectoJMM.penyas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoJMM.penyas.modelo.Jugador;
import proyectoJMM.penyas.repositorio.JugadorRepositorio;


import java.util.List;

//con esto ya podemos usar la capa de repositorio
@Service
public class JugadorServicio implements IJugadorServicio {
    @Autowired
    private JugadorRepositorio jugadorRepositorio;

    @Override
    public List<Jugador> listarJugadores() {
        return this.jugadorRepositorio.findAll();
    }
    public List<Jugador> obtenerJugadoresPorPenya(Integer idPenya) {
        return this.jugadorRepositorio.findByPenyaIdPenya(idPenya);
    }

    @Override
    public Jugador buscarJugadorPorId(Integer idJugador) {
        Jugador jugador1=this.jugadorRepositorio.findById(idJugador).orElse(null);
        return jugador1;
    }

    @Override
    public Jugador guardarJugador(Jugador jugador) {
        return this.jugadorRepositorio.save(jugador); //si el idJugador(PK) es nulo->insert sino->update
    }


    @Override
    public void borrarJugadorPorId(Integer idJugador) {
        this.jugadorRepositorio.deleteById(idJugador);
    }
    public void borrarJugadores(List<Jugador> jugadores) {
        for (Jugador jugador : jugadores) {
            jugadorRepositorio.deleteById(jugador.getIdJugador());
        }
    }
}
