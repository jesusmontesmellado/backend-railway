package proyectoJMM.penyas.servicio;

import proyectoJMM.penyas.modelo.Equipo;


import java.util.List;

public interface IEquipoServicio {

    //TODO ver que metodos hay que hacer realmente
    public  List<Equipo> listarEquipos();

    public Equipo buscarEquipoPorId(Integer idEquipo);

    //este metodo va hacer funcion insertar y actualizar, ya que la implementacion por defecto de hibernate,
    // si el idPenya es nulo hara insert y sino es nula hara update
    public Equipo guardarEquipo(Equipo equipo);

    public void borrarEquipoPorId(Integer idEquipo);

}
