package proyectoJMM.penyas.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoJMM.penyas.modelo.Penya;
import proyectoJMM.penyas.repositorio.PenyaRepositorio;

import java.util.List;
//con esto ya podemos usar la capa de repositorio
@Service
public class PenyaServicio implements IPenyaServicio{
    @Autowired
    private PenyaRepositorio penyaRepositorio;

    @Override
    public List<Penya> listarPenyas() {
        return this.penyaRepositorio.findAll();
    }

    @Override
    public Penya buscarPenyaPorId(Integer idPenya) {
        Penya penya=this.penyaRepositorio.findById(idPenya).orElse(null);
        return penya;
    }

    @Override
    public Penya guardarPenya(Penya penya) {
       return this.penyaRepositorio.save(penya); //si el idPenya(PK) es nulo->insert sino->update
    }

    @Override
    public void borrarPenyaPorId(Integer idPenya) {
        this.penyaRepositorio.deleteById(idPenya);
    }
}
