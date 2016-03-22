package com.example.micaela.db.Controladores;

import android.content.Context;

import com.example.micaela.db.DAO.GeneralDAO;
import com.example.micaela.db.Interfaces.IGeneral;
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
public class IGeneralImpl implements IGeneral {

    private GeneralDAO mGeneralDAO;

    public IGeneralImpl(){}

    public IGeneralImpl(Context context){

        mGeneralDAO = new GeneralDAO(context);
    }

    @Override
    public Estados getEstado(String situacion) throws ParseException {
        return mGeneralDAO.getEstado(situacion);
    }


    @Override
    public Personas getPersona(String email) throws ParseException {
        return mGeneralDAO.getPersona(email);
    }

    @Override
    public List<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object ) {
        return mGeneralDAO.getComentarios(listComentarios, object);
    }

    @Override
    public ParseObject agregarComentario(String comentario, String email, Context context) throws ParseException {
        return mGeneralDAO.agregarComentario(comentario, email, context);
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {
        return mGeneralDAO.getComentarioById(objectId);
    }

    @Override
    public boolean internet(Context context) {
        mGeneralDAO = new GeneralDAO(context);
        return mGeneralDAO.internet(context);
    }


    @Override
    public void save(ParseObject object) {

    }

    @Override
    public void delete(ParseObject object) {

    }

    @Override
    public void checkInternetGet(ParseQuery<ParseObject> query) {

    }

    @Override
    public List<Estados> getEstados() {

        return mGeneralDAO.getEstados();
    }

    @Override
    public void cambiarEstado(String idpublicacion, boolean estado) {
        mGeneralDAO.cambiarEstado(idpublicacion, estado);
    }

    @Override
    public void denunciar(String id, String motivo) throws ParseException{

        mGeneralDAO.denunciar(id, motivo);
    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {
        return mGeneralDAO.getMotivoDenuncia(motivo);
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {

        return mGeneralDAO.getMotivoDenuncias();
    }

    @Override
    public void startAlert() {
        mGeneralDAO.startAlert();
    }

}