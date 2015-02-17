package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import models.ClientDetails;
import models.NextOfKin;
import org.json.simple.JSONObject;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(ClientDetails.fetchAllClients()));
    }

    public static Result saveClient(){
        JSONObject jsnObj = new JSONObject();
        jsnObj.put("status", "SUCCESS");

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.dateOfBirth = getDate(values.get("dateOfBirth")[0]).getTime();
        clientDetails.emailAddress = values.get("emailAddress")[0];
        clientDetails.firstName = values.get("firstName")[0];
        clientDetails.lastName = values.get("lastName")[0];
        clientDetails.nationalId = values.get("nationalId")[0];
        clientDetails.nationality = values.get("nationality")[0];
        clientDetails.occupation = values.get("occupation")[0];
        clientDetails.phoneNumber = values.get("phoneNumber")[0];
        clientDetails.physicalAddress = values.get("physicalAddress")[0];
        clientDetails.save();

        return ok(Json.toJson(jsnObj));
    }

    public static Result addNextOfKin(){
        JSONObject jsnObj = new JSONObject();
        jsnObj.put("status", "SUCCESS");

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        NextOfKin nextOfKin = new NextOfKin();
        nextOfKin.emailAddress = values.get("emailAddress")[0];
        nextOfKin.firstName = values.get("firstName")[0];
        nextOfKin.lastName = values.get("lastName")[0];
        nextOfKin.nationalId = values.get("nationalId")[0];
        nextOfKin.phoneNumber = values.get("phoneNumber")[0];
        nextOfKin.relationship = values.get("relationship")[0];
        nextOfKin.save();

        return ok(Json.toJson(jsnObj));
    }

    public static Result uploadIdImage(){
        JSONObject result = new JSONObject();
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        ClientDetails clientDetails = ClientDetails.findByClientId(Long.parseLong(values.get("clientId")[0]));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        if(body == null){
            result.put("status", "Null body");
        } else {
            File file = body.getFiles().get(0).getFile();
            String extension = body.getFiles().get(0).getContentType().split("image/")[1];
            if(file != null){
                String myUploadPath = Play.application().configuration().getString("myUploadPath");
                String photoName = clientDetails.nationalId+"."+extension;
                file.renameTo(new File(myUploadPath, photoName));

                clientDetails.nationalIdPhoto = photoName;
                clientDetails.update();
                result.put("status", "SUCCESS");
            } else {
                result.put("status", "file.upload.failed.parsing.error");
            }
        }
        return ok(Json.toJson(result));
    }

    public static Result getClientsRecJSN(){
        return ok(Json.toJson(ClientDetails.fetchAllClients()));
    }

    public static Result getClientRecHSN(String clientId){
        return ok(Json.toJson(ClientDetails.findByClientId(Long.parseLong(clientId))));
    }

    private static Date getDate(String strDate){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}