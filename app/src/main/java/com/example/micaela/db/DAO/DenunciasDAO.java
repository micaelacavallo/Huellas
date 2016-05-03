package com.example.micaela.db.DAO;

import android.content.Context;

import com.example.micaela.db.Constantes.CDenuncias;
import com.example.micaela.db.Constantes.CMotivo_denuncia;
import com.example.micaela.db.Constantes.Clases;
import com.example.micaela.db.Controladores.IGeneralImpl;
import com.example.micaela.db.Interfaces.IDBLocal;
import com.example.micaela.db.Interfaces.IDenuncias;
import com.example.micaela.db.Modelo.MotivoDenuncia;
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
public class DenunciasDAO extends IGeneralImpl implements IDenuncias, IDBLocal {


    private Context context;
    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private ParseRelation objectRelation;
    private List<ParseObject> listParseObject;

    public DenunciasDAO() {
    }

    public DenunciasDAO(Context context) {
        this.context = context;
        query = null;
        objectAux = null;
        objectRelation = null;
        listParseObject = null;
    }

    @Override
    public void denunciar(String id, String motivo) throws ParseException {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.whereEqualTo(CDenuncias.ID_REFERENCIA, id);

        if(query.count() == 0) {

            MotivoDenuncia motivoDenuncia = this.getMotivoDenuncia(motivo);

            ParseObject objectAux = new ParseObject(Clases.DENUNCIAS);
            objectAux.put(CDenuncias.FECHA, new Date());
            objectAux.put(CDenuncias.ID_REFERENCIA, id);
            objectAux.put(CDenuncias.IS_USER, false);
            objectAux.put(CDenuncias.MOTIVO_DENUNCIA, ParseObject.createWithoutData(Clases.MOTIVODENUNCIA, String.valueOf(motivoDenuncia.getmObjectId())));

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

    @Override
    public void borrarDenuncia(String denunciaObjectId) {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.whereEqualTo(CDenuncias.OBJECT_ID, denunciaObjectId);
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


}
