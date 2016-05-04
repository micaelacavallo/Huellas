package com.example.micaela.db.DAO;
import android.content.Context;

import com.example.micaela.db.Constantes.CComentarios;
import com.example.micaela.db.Constantes.CPerdidos;
import com.example.micaela.db.Constantes.CPersonas;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Interfaces.IComentarios;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IGeneral;
import com.example.micaela.db.Modelo.Comentarios;
import com.example.micaela.db.Modelo.Personas;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 5/2/2016.
 */
public class ComentariosDAO extends IGeneralImpl implements IComentarios, IGeneral, IDBLocal {

    private Context context;
    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private ParseObject object;
    private List<Comentarios> comentarios;
    private List<ParseObject> listParseObject;
    private Comentarios comentario;
    private Personas persona;
    private IGeneral iGeneral;

    public ComentariosDAO() {
    }

    public ComentariosDAO(Context context) {
        this.context = context;
        objectAux = null;
        comentarios = new ArrayList<Comentarios>();
        iGeneral = new IGeneralImpl(context);
        listParseObject = null;
        comentario = null;
        persona = null;
        object = null;
    }

    @Override
    public ArrayList<Comentarios> getComentarios(List<ParseObject> listComentarios, ParseObject object) {

        Comentarios comentario = null;
        ParseObject objectAux = null;
        Personas persona = null;
        ArrayList<Comentarios> comentarios = null;

        if (listComentarios != null) {
            comentarios = new ArrayList<>();
            for (ParseObject objectComentario : listComentarios) {
                objectAux = objectComentario.getParseObject(CComentarios.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));
                comentario = new Comentarios(objectComentario.getObjectId(), objectComentario.getString(CComentarios.COMENTARIO), persona, objectComentario.getDate(CComentarios.FECHA), objectComentario.getBoolean(CComentarios.LEIDO));
                comentarios.add(comentario);
            }
        }
        return comentarios;
    }

    @Override
    public ParseObject getComentarioById(String objectId) throws ParseException {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.include(CComentarios.ID_PERSONA);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        iGeneral.checkInternetGet(query);

        try {
            if(query.count() != 0) {
                object = query.getFirst();
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
        return object;

    }
    @Override
    public List<Comentarios> getComentariosNoLeidos(String userObjectId) {

        ParseObject obj = ParseObject.createWithoutData(Clases.PERSONAS, userObjectId);

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.include(CComentarios.ID_PERSONA);
        query.whereEqualTo(CComentarios.ID_PERSONA, obj);
        query.whereEqualTo(CComentarios.LEIDO, false);

        checkInternetGet(query);

        try{
            listParseObject = query.find();
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        if (listParseObject.size() > 0) {
            for (ParseObject object : listParseObject) {
                objectAux = object.getParseObject(CPerdidos.ID_PERSONA);
                persona = new Personas(objectAux.getObjectId(), objectAux.getString(CPersonas.EMAIL), objectAux.getString(CPersonas.NOMBRE), objectAux.getString(CPersonas.TELEFONO), objectAux.getBoolean(CPersonas.ADMINISTRADOR), objectAux.getBoolean(CPersonas.BLOQUEADO), objectAux.getString(CPersonas.CONTRASEÑA), objectAux.getString(CPersonas.FOTO));

                comentario = new Comentarios(object.getObjectId(), object.getString(CComentarios.COMENTARIO), persona, object.getDate(CComentarios.FECHA), object.getBoolean(CComentarios.LEIDO));
                comentarios.add(comentario);
            }
        }


        return comentarios;
    }

    @Override
    public void cambiarLeidoComentario(String comentarioObjectId, boolean leido) {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, comentarioObjectId);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                objectAux.put(CComentarios.LEIDO, leido);
                save(objectAux);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }

    }

    @Override
    public void borrarComentario(String objectId) {

        query = ParseQuery.getQuery(Clases.COMENTARIOS);
        query.whereEqualTo(CComentarios.OBJECT_ID, objectId);
        checkInternetGet(query);
        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                delete(objectAux);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pinObjectInBackground(ParseObject object) {
        object.pinInBackground();
    }

    @Override
    public void unpinObjectInBackground(ParseObject object) {
        object.unpinInBackground();
    }

    @Override
    public void queryFromLocalDatastore(ParseQuery query) {
        query.fromLocalDatastore();
    }

    @Override
    public void saveEventually(ParseObject object) {
        object.saveEventually();
    }

    @Override
    public void saveInBackground(ParseObject object) {

        object.saveInBackground();
    }

    @Override
    public void deleteEventually(ParseObject object) {
        object.deleteEventually();
    }

    @Override
    public void deleteInBackground(ParseObject object) {
        object.deleteInBackground();
    }

    @Override
    public void cargarDBLocal(Context context) throws ParseException {

    }
}
