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
        ObjectNode jsnObj = Json.newObject();
        jsnObj.put("status", "SUCCESS");

        JsonNode json = request().body().asJson();

        System.out.print("New Values.....    "+json.toString());

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.dateOfBirth = json.get("dateOfBirth").asLong();
        clientDetails.emailAddress = json.get("emailAddress").asText();
        clientDetails.firstName = json.get("firstName").asText();
        clientDetails.lastName = json.get("lastName").asText();
        clientDetails.nationalId = json.get("nationalId").asText();
        clientDetails.nationality = json.get("nationality").asText();
        clientDetails.occupation = json.get("occupation").asText();
        clientDetails.phoneNumber = json.get("phoneNumber").asText();
        clientDetails.physicalAddress = json.get("physicalAddress").asText();
        clientDetails.save();

        return ok(jsnObj);
    }

    public static Result updateClient(){
        ObjectNode jsnObj = Json.newObject();
        jsnObj.put("status", "SUCCESS");

        JsonNode json = request().body().asJson();

        System.out.print("Update Values.....    "+json.toString());

        ClientDetails clientDetails = new ClientDetails();
        clientDetails.clientId = json.get("clientId").asLong();
        clientDetails.dateOfBirth = json.get("dateOfBirth").asLong();
        clientDetails.emailAddress = json.get("emailAddress").asText();
        clientDetails.firstName = json.get("firstName").asText();
        clientDetails.lastName = json.get("lastName").asText();
        clientDetails.nationalId = json.get("nationalId").asText();
        clientDetails.nationality = json.get("nationality").asText();
        clientDetails.occupation = json.get("occupation").asText();
        clientDetails.phoneNumber = json.get("phoneNumber").asText();
        clientDetails.physicalAddress = json.get("physicalAddress").asText();
        clientDetails.update();

        return ok(jsnObj);
    }

    public static Result addNextOfKin(){
        ObjectNode jsnObj = Json.newObject();
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

        return ok(jsnObj);
    }

    public static Result uploadIdImage(){
        ObjectNode result = Json.newObject();
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        ClientDetails clientDetails = ClientDetails.findByClientId(Long.parseLong(values.get("clientId")[0]));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        if(body == null){
            result.put("status", "Null body");
        } else {
            File file = body.getFiles().get(0).getFile();
            String extension = body.getFiles().get(0).getContentType().split("image/")[1];
            if(file != null){
                String myUploadPath = Play.application().configuration().getString("clientIdPhotosPath");
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

    public static Result getClientRecHSN(String clientId) {
        return ok(Json.toJson(ClientDetails.findByClientId(Long.parseLong(clientId))));
    }
}