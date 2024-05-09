package org.launchcode.homebase.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.json.JSONArray;
import org.json.JSONObject;
import org.launchcode.homebase.data.EmailNotificationRepository;
import org.launchcode.homebase.data.EquipmentRepository;
import org.launchcode.homebase.data.FilterRepository;
import org.launchcode.homebase.data.UserRepository;
import org.launchcode.homebase.exception.ResourceNotFoundException;
import org.launchcode.homebase.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.scheduling.annotation.Scheduled;


import java.io.IOException;
import com.sendgrid.*;

import java.util.*;

@Service
public class EmailService {
    @Autowired
    private EmailNotificationRepository emailNotificationRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private FilterService filterService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SerpApiService serpApiService;

    private SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//    Bash commands for updating
//    echo "sendgrid.env" >> .gitignore
//    echo "export SENDGRID_API_KEY='YOUR_API_KEY'" > sendgrid.env
//    source ./sendgrid.env


//    public void sendEmail(int id, int filterId, String email, String filterChangeReminder, String s, Map<String, Object> templateData) throws Exception {
//        try {
//            Mail mail = new Mail();
//
//            Email from = new Email("kenjigw@gmail.com");
//            mail.setFrom(from);
//
//            Email to = new Email("abwashingstl@gmail.com");
//
//            String subject = "test";
//            mail.setSubject(subject);
//
//            Personalization personalization = new Personalization();
//            personalization.addTo(to);
//            personalization.setSubject(subject);
//            personalization.addDynamicTemplateData("user", "Billy");
//            personalization.addDynamicTemplateData("equipment", "Closet");
//            personalization.addDynamicTemplateData("location", "Summer Home");
//            personalization.addDynamicTemplateData("filter", "15x30 Allergy");
//            mail.addPersonalization(personalization);
//            mail.setTemplateId("d-1ca98b984e2e48ac9ae12fd3fb8b7aa1");
//
//            // Build the request and send the email
//            Request request = new Request();
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//
//            Response response = sg.api(request);
//
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            throw new Exception("Failed to send email: " + ex.getMessage());
//        }
//    }

    public void sendEmail(EmailRequest emailRequest) throws Exception {
        try {
            Mail mail = new Mail();

            Email from = new Email("kenjigw@gmail.com");
            mail.setFrom(from);

            Email to = new Email(emailRequest.getTo()); // Use the 'to' field from the EmailRequest object

            String subject = emailRequest.getSubject(); // Use the 'subject' field from the EmailRequest object
            mail.setSubject(subject);

            Personalization personalization = new Personalization();
            personalization.addTo(to);
            personalization.setSubject(subject);

            // Get the JSONObject template data
            JSONObject templateData = emailRequest.getTemplateData();

            // Create a new JSONObject to hold the dynamic template data
            JSONObject dynamicTemplateData = new JSONObject();

            // Add dynamic template data from the JSONObject
            Iterator<String> keys = templateData.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                personalization.addDynamicTemplateData(key, templateData.get(key));
            }
            System.out.println("templateData: " + templateData);
            mail.addPersonalization(personalization);
            mail.setTemplateId("d-1ca98b984e2e48ac9ae12fd3fb8b7aa1"); // Ensure this is the correct template ID

            // Build the request and send the email
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw new Exception("Failed to send email: " + ex.getMessage());
        }
    }


    private void logEmailNotification(int equipmentId, int filterId, String to, String subject, String message) {

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with id = " + equipmentId));

        Filter filter = filterRepository.findById(filterId)
                .orElseThrow(() -> new ResourceNotFoundException("Filter not found with id = " + filterId));

        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setRecipientEmail(to);
        emailNotification.setEquipmentId(equipmentId);
        emailNotification.setEquipmentName(equipment.getName());
        emailNotification.setFilterId(filterId);
        emailNotification.setSentTimestamp(new Date());

        emailNotificationRepository.save(emailNotification);

    }

    @Scheduled(cron = "0 0 5 * * ?") // Run every day at 5 am
    public void sendEmailsForDueFilters() {
        try {

            List<Filter> filtersDueForChange = filterService.getFiltersToChangeInNext7Days();

            User user = userRepository.findById(1).orElse(null);
            System.out.println("Number of filters due for change: " + filtersDueForChange.size());
            System.out.println("User exists: " + (user != null));
            if(user == null) {
                System.out.println("User does not exist");
            }
            if(user != null) {
                for (Filter filter : filtersDueForChange) {
                    sendEmailForFilterChange(filter, user);
                }
            }
        } catch (Exception ex) {
            System.err.println("Error sending emails for due filters: " + ex.getMessage());
        }
    }

    private void sendEmailForFilterChange(Filter filter, User user) throws Exception {
        // Create email content and subject
        System.out.println("Sending email for filter change...");
        System.out.println("User email: " + user.getEmail());
        System.out.println("Filter equipment name: " + filter.getEquipment().getName());

        // Fetch top 3 Google Shopping results using SERP API
        String searchQuery = "filter size" + filter.getHeight() + "x" + filter.getWidth() + "x" + filter.getLength(); // Use equipment name as search query
        JsonNode serpApiResults = serpApiService.getGoogleShoppingResults(searchQuery);

        // Create the JSON object
        JSONObject jsonData = new JSONObject();
        jsonData.put("user", user.getUsername());
        jsonData.put("equipment", filter.getEquipment().getName());
        jsonData.put("location", filter.getLocation());
        jsonData.put("size", filter.getHeight() + "x" + filter.getWidth() + "x" + filter.getLength());

        // Add topResults data
        JSONArray topResultsArray = new JSONArray();
        if (serpApiResults != null && serpApiResults.has("shopping_results")) {
            JsonNode shoppingResults = serpApiResults.get("shopping_results");
            System.out.println("Shopping Results: " + shoppingResults);
            int count = 1;
            for (JsonNode result : shoppingResults) {
                if (count > 3) {
                    break; // Include only the top 3 results
                }
                JSONObject resultObj = new JSONObject();
                resultObj.put("title", result.get("title").asText());
                resultObj.put("link", result.get("link").asText());
                topResultsArray.put(resultObj);
                count++;
            }
        }
        jsonData.put("topResults", topResultsArray);
        System.out.println("Template Data: " + jsonData.toString());


        // Construct the EmailRequest object
        EmailRequest emailRequest = new EmailRequest(
                filter.getEquipment().getId(),
                filter.getId(),
                user.getEmail(),
                "Filter Change Reminder",
                "Your filter for " + filter.getEquipment().getName() + " is due for change.",
                jsonData
        );

        // Send email
        sendEmail(emailRequest);

        // Log email notification
        logEmailNotification(
                filter.getEquipment().getId(),
                filter.getId(),
                user.getEmail(),
                "Filter Change Reminder",
                "Your filter for " + filter.getEquipment().getName() + " is due for change."
        );
    }

//    private void sendEmailForFilterChange(Filter filter, User user) throws Exception {
//        // Create email content and subject
//        System.out.println("Sending email for filter change...");
//        System.out.println("User email: " + user.getEmail());
//        System.out.println("Filter equipment name: " + filter.getEquipment().getName());
//
//        // Fetch top 3 Google Shopping results using SERP API
//        String searchQuery = "filter size" + filter.getHeight() + "x" + filter.getWidth() + "x" + filter.getLength(); // Use equipment name as search query
//        JsonNode serpApiResults = serpApiService.getGoogleShoppingResults(searchQuery);
//
//        // Prepare template data
//        Map<String, Object> templateData = new HashMap<>();
//        templateData.put("user", user.getUsername()); // Assuming User has a getUsername() method
//        templateData.put("equipment", filter.getEquipment().getName());
//        templateData.put("location", filter.getLocation()); // Assuming Filter has a getLocation() method
//        templateData.put("size", filter.getHeight() + "x" + filter.getWidth() + "x" + filter.getLength()); // Assuming Filter has getHeight(), getWidth(), and getLength() methods
//
//        // Include top 3 results in the template data
//        List<Map<String, String>> topResults = new ArrayList<>();
//        if (serpApiResults != null && serpApiResults.has("shopping_results")) {
//            JsonNode shoppingResults = serpApiResults.get("shopping_results");
//            System.out.println("Shopping Results: " + shoppingResults);
//            int count = 1;
//            for (JsonNode result : shoppingResults) {
//                if (count > 3) {
//                    break; // Include only the top 3 results
//                }
//                Map<String, String> resultMap = new HashMap<>();
//                resultMap.put("title", result.get("title").asText());
//                resultMap.put("link", result.get("link").asText());
//                topResults.add(resultMap);
//                count++;
//            }
//        }
//        templateData.put("topResults", topResults);
//        System.out.println("TemplateData:" + templateData);
//        // Construct the EmailRequest object
//        EmailRequest emailRequest = new EmailRequest(
//                filter.getEquipment().getId(),
//                filter.getId(),
//                user.getEmail(),
//                "Filter Change Reminder",
//                "Your filter for " + filter.getEquipment().getName() + " is due for change.",
//                templateData
//        );
//
//        // Send email
//        sendEmail(emailRequest);
//
//        // Log email notification
//        logEmailNotification(
//                filter.getEquipment().getId(),
//                filter.getId(),
//                user.getEmail(),
//                "Filter Change Reminder",
//                "Your filter for " + filter.getEquipment().getName() + " is due for change."
//        );
//    }


}





