package com.example.micaela.db.DAO;

import android.content.Context;

import com.example.micaela.db.Constantes.CAdicionales;
import com.example.micaela.db.Constantes.CDenuncias;
import com.example.micaela.db.Constantes.CMotivo_denuncia;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IAdicionalesImpl;
import com.example.micaela.db.Controladores.IComentariosImpl;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Controladores.IPerdidosImpl;
import com.example.micaela.db.Controladores.IPersonasImpl;
import com.example.micaela.db.Interfaces.IAdicionales;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Interfaces.IDB;
import com.example.micaela.db.Interfaces.IDenuncias;
import com.example.micaela.db.Interfaces.IPerdidos;
import com.example.micaela.db.Interfaces.IPersonas;
import com.example.micaela.db.Modelo.Adicionales;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Denuncias;
import com.example.micaela.db.Modelo.MotivoDenuncia;
import com.example.micaela.db.Modelo.Perdidos;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class DenunciasDAO extends IGeneralImpl implements IDenuncias, IDB {


    private Context context;
    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private ParseRelation objectRelation;
    private List<ParseObject> listParseObject;
    private Denuncias denuncia;
    private Personas persona;
    private List<Denuncias> denuncias;
    private MotivoDenuncia motivoDenuncia;
    private IPerdidos iPerdidos;
    private IAdicionales iAdicionales;
    private IPersonas iPersonas;
    private IComentarios iComentarios;
    private List<Perdidos> perdidos;
    private Perdidos perdido;
    private Adicionales adicional;
    private List<Adicionales> adicionales;
    private List<Comentarios> comentarios;
    private Comentarios comentario;

    public DenunciasDAO() {
    }

    public DenunciasDAO(Context context) {
        this.context = context;
        query = null;
        objectAux = null;
        objectRelation = null;
        listParseObject = null;
        denuncia = null;
        perdido = null;
        persona = null;
        adicional = null;
        comentario = null;
        comentarios = null;
        perdidos = null;
        adicionales = null;
        denuncias = new ArrayList<Denuncias>();
        motivoDenuncia = null;
        iPerdidos = new IPerdidosImpl(context);
        iAdicionales = new IAdicionalesImpl(context);
        iPersonas = new IPersonasImpl(context);
        iComentarios = new IComentariosImpl(context);
    }

    @Override
    public void denunciar(String id, String motivo, String tabla) throws ParseException {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.whereEqualTo(CDenuncias.ID_REFERENCIA, id);

        if(query.count() == 0) {

            MotivoDenuncia motivoDenuncia = this.getMotivoDenuncia(motivo);

            ParseObject objectAux = new ParseObject(Clases.DENUNCIAS);
            objectAux.put(CDenuncias.FECHA, new Date());
            objectAux.put(CDenuncias.ID_REFERENCIA, id);
            objectAux.put(CDenuncias.CONTADOR, 1);
            if(tabla.equals("Personas")){
            objectAux.put(CDenuncias.IS_USER, true);
            }else {
                objectAux.put(CDenuncias.IS_USER, false);
            }

            objectAux.put(CDenuncias.TABLA, tabla);
            objectAux.put(CDenuncias.MOTIVO_DENUNCIA, ParseObject.createWithoutData(Clases.MOTIVODENUNCIA, String.valueOf(motivoDenuncia.getmObjectId())));

            save(objectAux);
        }
        else{
            objectAux = query.getFirst();
            int contador = objectAux.getInt(CDenuncias.CONTADOR) + 1;
            objectAux.put(CDenuncias.CONTADOR, contador);
            save(objectAux);
        }

    }

    @Override
    public MotivoDenuncia getMotivoDenuncia(String motivo) {

        query = ParseQuery.getQuery(Clases.MOTIVODENUNCIA);
        query.whereEqualTo(CMotivo_denuncia.MOTIVO, motivo);
        MotivoDenuncia motivoDenuncia = null;
        try {
            if(query.count() != 0) {
                ParseObject object = query.getFirst();
                motivoDenuncia = new MotivoDenuncia(object.getObjectId(), object.getString(CMotivo_denuncia.MOTIVO));
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

        return motivoDenuncia;
    }

    @Override
    public List<MotivoDenuncia> getMotivoDenuncias() {

        query = ParseQuery.getQuery(Clases.MOTIVODENUNCIA);
        List<MotivoDenuncia> listMotivoDenuncia = new ArrayList<MotivoDenuncia>();
        List<ParseObject> listParseObject = null;
        MotivoDenuncia motivoDenuncia = null;
        if(internet(context)) {
            try {
                listParseObject = query.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (ParseObject object : listParseObject) {
                motivoDenuncia = new MotivoDenuncia(object.getObjectId(), object.getString(CMotivo_denuncia.MOTIVO));
                listMotivoDenuncia.add(motivoDenuncia);
            }
        }
        return listMotivoDenuncia;
    }

    public List<Denuncias> getListDenuncias(List<ParseObject> listParseObject) throws ParseException {

        if (listParseObject.size() > 0) {
            for (ParseObject object : listParseObject) {

                denuncia = getDenuncia(object);
                denuncias.add(denuncia);
            }
        }

        return denuncias;
    }

    public Denuncias getDenuncia(ParseObject object){

        objectAux = object.getParseObject(CDenuncias.MOTIVO_DENUNCIA);
        motivoDenuncia = new MotivoDenuncia(objectAux.getObjectId(),objectAux.getString(CMotivo_denuncia.MOTIVO));
        denuncia = new Denuncias(object.getObjectId(), object.getBoolean(CDenuncias.IS_USER), object.getDate(CDenuncias.FECHA), object.getString(CDenuncias.ID_REFERENCIA), motivoDenuncia, object.getString(CDenuncias.TABLA), object.getInt(CDenuncias.CONTADOR));

        return denuncia;
    }


    @Override
    public void saveInBackground(ParseObject object) {

        object.saveInBackground();
    }

    @Override
    public void deleteInBackground(ParseObject object) {
        object.deleteInBackground();
    }

    @Override
    public void pinObjectInBackground(ParseObject object) {
        object.pinInBackground();
    }

    @Override
    public void unpinObjectInBackground(ParseObject object) {
        object.unpinInBackground();
    }


}
