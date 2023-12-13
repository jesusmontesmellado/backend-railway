package proyectoJMM.penyas.servicio;

import proyectoJMM.penyas.modelo.Jugador;


import java.util.List;

public interface IJugadorServicio {
    public  List<Jugador> listarJugadores();

    public  Jugador buscarJugadorPorId(Integer idJugador);

    //este metodo va hacer funcion insertar y actualizar, ya que la implementacion por defecto de hibernate,
    // si el idPenya es nulo hara insert y sino es nula hara update
    public Jugador guardarJugador(Jugador jugador);

    public void borrarJugadorPorId(Integer idJugador);

}
