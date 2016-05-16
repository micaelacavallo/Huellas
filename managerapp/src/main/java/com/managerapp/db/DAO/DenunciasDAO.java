package com.managerapp.db.DAO;

import android.content.Context;

import com.managerapp.db.Constantes.CAdicionales;
import com.managerapp.db.Constantes.CDenuncias;
import com.managerapp.db.Constantes.CMotivo_denuncia;
import com.managerapp.db.Constantes.CPerdidos;
import com.managerapp.db.Constantes.CPersonas;
import com.managerapp.db.Constantes.Clases;
import com.managerapp.db.Controladores.IAdicionalesImpl;
import com.managerapp.db.Controladores.IGeneralImpl;
import com.managerapp.db.Controladores.IPerdidosImpl;
import com.managerapp.db.Controladores.IPersonasImpl;
import com.managerapp.db.Interfaces.IAdicionales;
import com.managerapp.db.Interfaces.IDBLocal;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Interfaces.IPersonas;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.MotivoDenuncia;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.db.Modelo.Personas;
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
    private Denuncias denuncia;
    private Personas persona;
    private Adicionales adicional;
    private Perdidos perdido;
    private List<Denuncias> denuncias;
    private MotivoDenuncia motivoDenuncia;
    private IPerdidos iPerdidos;
    private IAdicionales iAdicionales;
    private IPersonas iPersonas;

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
        denuncias = new ArrayList<Denuncias>();
        motivoDenuncia = null;
        iPerdidos = new IPerdidosImpl(context);
        iAdicionales = new IAdicionalesImpl(context);
        iPersonas = new IPersonasImpl(context);
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
            if(tabla.equals("Personas")){
                objectAux.put(CDenuncias.IS_USER, true);
            }else {
                objectAux.put(CDenuncias.IS_USER, false);
            }

            objectAux.put(CDenuncias.TABLA, tabla);
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
    public void confirmarDenuncia(String denunciaObjectId) throws ParseException {

        denuncia = getDenunciaById(denunciaObjectId);
        if(denuncia.ismUser()){
            objectAux = iPersonas.getParseObjectById(denuncia.getmId());
            objectAux.put(CPersonas.BLOQUEADO, true);
        }
        else
        {
            if(denuncia.getmTabla().equals("Perdidos")){
                objectAux = iPerdidos.getParseObjectById(denuncia.getmId());
                objectAux.put(CPerdidos.BLOQUEADO, true);
            }else{ //adicional
                objectAux = iAdicionales.getParseObjectById(denuncia.getmId());
                objectAux.put(CAdicionales.BLOQUEADO, true);
            }
        }

        save(objectAux);

    }

    @Override
    public Denuncias getDenunciaById(String objectId) {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.include(CDenuncias.MOTIVO_DENUNCIA);
        query.orderByDescending(CDenuncias.FECHA);
        checkInternetGet(query);

        try {
            if(query.count() != 0) {
                objectAux = query.getFirst();
                denuncia = getDenuncia(objectAux);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return denuncia;

    }

    @Override
    public List<Denuncias> getDenuncias() throws ParseException {

        query = ParseQuery.getQuery(Clases.DENUNCIAS);
        query.include(CDenuncias.MOTIVO_DENUNCIA);
        query.orderByDescending(CDenuncias.FECHA);
        checkInternetGet(query);

        try {
            listParseObject = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getListDenuncias(listParseObject);
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
        denuncia = new Denuncias(object.getObjectId(), object.getBoolean(CDenuncias.IS_USER), object.getDate(CDenuncias.FECHA), object.getString(CDenuncias.ID_REFERENCIA), motivoDenuncia, object.getString(CDenuncias.TABLA));

        return denuncia;
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
