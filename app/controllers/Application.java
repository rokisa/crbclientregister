package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import models.ClientDetails;
import models.NextOfKin;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import play.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.apache.commons.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(ClientDetails.fetchAllClients()));
    }

    public static Result saveClient(){
        ObjectNode jsnObj = Json.newObject();
        jsnObj.put("status", "SUCCESS");

        JsonNode json = request().body().asJson();

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
        String val = Form.form().
                bindFromRequest().get("clientId");

        ClientDetails clientDetails = ClientDetails.findByClientId(Long.parseLong(val));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        if(body == null){
            result.put("status", "Null body");
        } else {
            File file = body.getFiles().get(0).getFile();
            String extension = body.getFiles().get(0).getContentType().split("image/")[1];
            if(file != null){
                String myUploadPath = Play.application().configuration().getString("clientIdPhotosPath");
                String photoName = clientDetails.nationalId+"."+extension;

                try {
                    Files.move(file.toPath(),new File(myUploadPath + File.separator + photoName).toPath());
                } catch (IOException e) {
                    try {
                        Files.delete(new File(myUploadPath + File.separator + photoName).toPath());
                        Files.move(file.toPath(),new File(myUploadPath + File.separator + photoName).toPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        e.printStackTrace();
                    }
                }

                clientDetails.nationalIdPhoto = photoName;
                clientDetails.update();
                result.put("status", "SUCCESS");
            } else {
                result.put("status", "file.upload.failed.parsing.error");
            }
        }
        return ok(Json.toJson(result));
    }

    public static Result uploadProfileImage(){
        ObjectNode result = Json.newObject();
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String val = Form.form().
                bindFromRequest().get("clientId");

        ClientDetails clientDetails = ClientDetails.findByClientId(Long.parseLong(val));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        if(body == null){
            result.put("status", "Null body");
        } else {
            File file = body.getFiles().get(0).getFile();
            String extension = body.getFiles().get(0).getContentType().split("image/")[1];
            if(file != null){
                String myUploadPath = Play.application().configuration().getString("clientPhotosPath");
                String photoName = clientDetails.nationalId+"."+extension;

                try {
                    Files.move(file.toPath(),new File(myUploadPath + File.separator + photoName).toPath());
                } catch (IOException e) {
                    try {
                        Files.delete(new File(myUploadPath + File.separator + photoName).toPath());
                        Files.move(file.toPath(),new File(myUploadPath + File.separator + photoName).toPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        e.printStackTrace();
                    }
                }

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
        ClientDetails clientDetails = ClientDetails.
                findByClientId(json.get("clientId").asLong());
        List<NextOfKin> nextOfKinList = NextOfKin.getNextOfKinByClient(clientDetails);

        return ok(Json.toJson(nextOfKinList));
    }

    public static Result getClientRecHSN(String clientId) {
        return ok(Json.toJson(ClientDetails.findByClientId(Long.parseLong(clientId))));
    }
}