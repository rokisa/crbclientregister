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

        System.out.print("New Values.....    "+json.get("emailAddress"));
        System.out.print("De Date Values.....    "+new Date(json.get("dateOfBirth").asLong()));

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

        System.out.print("Update Values.....    "+json.get("emailAddress"));
        System.out.print("New Date Of Birth is.....    "+new Date(json.get("dateOfBirth").asLong()));

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

        JsonNode json = request().body().asJson();
        ClientDetails clientDetails = new ClientDetails();
        clientDetails = ClientDetails.findByClientId(json.get("customerId").asLong());
        NextOfKin nextOfKin = new NextOfKin();
        nextOfKin.emailAddress = json.get("emailAddress").asText();
        nextOfKin.firstName = json.get("firstName").asText();
        nextOfKin.lastName = json.get("lastName").asText();
        nextOfKin.nationalId = json.get("nationalId").asText();
        nextOfKin.phoneNumber = json.get("phoneNumber").asText();
        nextOfKin.relationship = json.get("relationship").asText();
        nextOfKin.clientDetails = clientDetails;
        nextOfKin.save();

        return ok(jsnObj);
    }

    public static Result updateNextOfKin(){
        ObjectNode jsnObj = Json.newObject();
        jsnObj.put("status", "SUCCESS");

        JsonNode json = request().body().asJson();
        ClientDetails clientDetails = new ClientDetails();
        clientDetails = ClientDetails.findByClientId(json.get("customerId").asLong());
        NextOfKin nextOfKin = new NextOfKin();
        nextOfKin.nextOfKinId = json.get("nextOfKinId").asLong();
        nextOfKin.emailAddress = json.get("emailAddress").asText();
        nextOfKin.firstName = json.get("firstName").asText();
        nextOfKin.lastName = json.get("lastName").asText();
        nextOfKin.nationalId = json.get("nationalId").asText();
        nextOfKin.phoneNumber = json.get("phoneNumber").asText();
        nextOfKin.relationship = json.get("relationship").asText();
        nextOfKin.clientDetails = clientDetails;
        nextOfKin.update();

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

    public static Result getNextOfKinRecJSN(){
        JsonNode json = request().body().asJson();
        return ok(Json.toJson(ClientDetails.findByClientId(
                json.get("clientId").asLong()).nextOfKinList));
    }

    public static Result getClientRecHSN(String clientId) {
        return ok(Json.toJson(ClientDetails.findByClientId(Long.parseLong(clientId))));
    }
}