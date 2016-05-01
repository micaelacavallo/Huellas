package com.example.micaela.db.Interfaces;

import android.content.Context;

import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Estados;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Horacio on 10/10/2015.
 */
public interface IGeneral {

    public Estados getEstado(String situacion) throws ParseException;
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object);
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException;
    public ParseObject getComentarioById(String objectId) throws ParseException;
    public boolean internet(Context context);
    public void save(ParseObject object);
    public void delete(ParseObject object);
    public void checkInternetGet(ParseQuery<ParseObject> query);
    public List<Estados> getEstados ();
    public void cambiarEstado(String idpublicacion, boolean estado);
    public void denunciar(String id, String motivo) throws ParseException;
    public MotivoDenuncia getMotivoDenuncia(String motivo);
    public List<MotivoDenuncia> getMotivoDenuncias();
    public List<Comentarios> getComentariosNoLeidos(String userObjectId);
    public void cambiarLeidoComentario(String comentarioObjectId, boolean leido);
    public void borrarComentario(String objectId);
    public void startAlert();

}
