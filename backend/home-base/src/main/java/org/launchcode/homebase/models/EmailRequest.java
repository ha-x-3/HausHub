package org.launchcode.homebase.models;

import java.util.Map;
import org.json.JSONObject;

public class EmailRequest {

    private int equipmentId;
    private int filterId;
    private String to;
    private String subject;
    private String message;
    private JSONObject templateData;
    private Map imageUrls;

    public EmailRequest(int equipmentId, int filterId, String to, String subject, String message, JSONObject templateData, Map<String, String> imageUrls) {
        this.equipmentId = equipmentId;
        this.filterId = filterId;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.templateData = templateData;
        this.imageUrls = imageUrls;

    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Map imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getTemplateData() {
        return templateData;
    }

    public void setTemplateData(JSONObject templateData) {
        this.templateData = templateData;
    }
}
