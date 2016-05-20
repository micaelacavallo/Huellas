package com.managerapp.db.DAO;

import android.content.Context;

import com.managerapp.db.Constantes.CAdicionales;
import com.managerapp.db.Constantes.CPerdidos;
import com.managerapp.db.Constantes.CPersonas;
import com.managerapp.db.Constantes.Clases;
import com.managerapp.db.Controladores.IAdicionalesImpl;
import com.managerapp.db.Controladores.IComentariosImpl;
import com.managerapp.db.Controladores.IDenunciasImpl;
import com.managerapp.db.Controladores.IPerdidosImpl;
import com.managerapp.db.Controladores.IPersonasImpl;
import com.managerapp.db.Interfaces.IAdicionales;
import com.managerapp.db.Interfaces.IAdmin;
import com.managerapp.db.Interfaces.IComentarios;
import com.managerapp.db.Interfaces.IDenuncias;
import com.managerapp.db.Interfaces.IPerdidos;
import com.managerapp.db.Interfaces.IPersonas;
import com.managerapp.db.Modelo.Adicionales;
import com.managerapp.db.Modelo.Comentarios;
import com.managerapp.db.Modelo.Denuncias;
import com.managerapp.db.Modelo.MotivoDenuncia;
import com.managerapp.db.Modelo.Perdidos;
import com.managerapp.db.Modelo.Personas;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quimey.arozarena on 3/21/2016.
 */
public class AdminDAO implements IAdmin {

    private ParseQuery<ParseObject> query;
    private ParseObject objectAux;
    private GeneralDAO mGeneralDAO;
    private Context context;
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
    private IDenuncias iDenuncias;

    public AdminDAO(Context context){
        this.context = context;
        mGeneralDAO = new GeneralDAO(context);
        objectAux = null;
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
        iDenuncias = new IDenunciasImpl(context);
    }

    @Override
    public int login(String nombre, String contraseña){

        int result = 0;
        query = ParseQuery.getQuery(Clases.PERSONAS);
        query.whereEqualTo(CPersonas.NOMBRE, nombre);
        query.whereEqualTo(CPersonas.ADMINISTRADOR, true);
        checkInternetGet(query);

        try {
            if(query.count() == 0) {
                result = -1;
            }
            else{
                query = ParseQuery.getQuery(Clases.PERSONAS);
                query.whereEqualTo(CPersonas.NOMBRE, nombre);
                query.whereNotEqualTo(CPersonas.CONTRASEÑA, contraseña);
                checkInternetGet(query);
                if(query.count() != 0) {
                    result = -2;
                }
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public void bloquearPersona(String objectId) throws ParseException {

        ParseObject point = ParseObject.createWithoutData(Clases.PERSONAS, objectId);
        point.put(CPersonas.BLOQUEADO, true);
        save(point);

    }

    @Override
    public void save(ParseObject object) {

        if(internet(context)) {
            object.saveInBackground();
        }
        else
        {
            object.saveEventually();
        }

        object.pinInBackground();
    }

    @Override
    public void confirmarDenunciaPublicacion(String publicacionObjectId, String tabla) {

        try {
        denuncia = iDenuncias.getDenunciaByIdRef(publicacionObjectId);
        if(denuncia != null) {
            iDenuncias.confirmarDenuncia(denuncia.getmObjectId());
        }else{
            if(tabla == Clases.ADICIONALES) {
                adicional = iAdicionales.getAdicionalById(publicacionObjectId);
                objectAux = iAdicionales.getParseObjectById(adicional.getObjectId());
                objectAux.put(CAdicionales.BLOQUEADO, true);
            }else{
                perdido = iPerdidos.getPublicacionPerdidosById(publicacionObjectId);
                objectAux = iPerdidos.getParseObjectById(perdido.getObjectId());
                objectAux.put(CPerdidos.BLOQUEADO, true);
            }
            save(objectAux);
        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkInternetGet(ParseQuery<ParseObject> query) {
        if (!internet(context)) {
            query.fromLocalDatastore();
        }
    }

    public boolean internet(Context context) {
        mGeneralDAO = new GeneralDAO(context);
        return mGeneralDAO.internet(context);
    }
}
