package proyectoJMM.penyas.servicio;

import proyectoJMM.penyas.modelo.Penya;

import java.util.List;

public interface IPenyaServicio {
    public  List<Penya> listarPenyas();

    public  Penya buscarPenyaPorId(Integer idPenya);

    //este metodo va hacer funcion insertar y actualizar, ya que la implementacion por defecto de hibernate,
    // si el idPenya es nulo hara insert y sino es nula hara update
    public Penya guardarPenya(Penya penya);

    public void borrarPenyaPorId(Integer idPenya);

}
