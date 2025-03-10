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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.IOException;
import com.sendgrid.*;
import java.util.*;
import java.util.Date;

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

    public void sendEmail(EmailRequest emailRequest) throws Exception {
        try {
            Mail mail = new Mail();
            Email from = new Email("britbot3000@gmail.com");
            mail.setFrom(from);
            Email to = new Email(emailRequest.getTo());

            String subject = emailRequest.getSubject();
            mail.setSubject(subject);

            Personalization personalization = new Personalization();
            personalization.addTo(to);
            personalization.setSubject(subject);

            // Get the JSONObject template data
            JSONObject templateData = emailRequest.getTemplateData();

            // Add substitutions for dynamic values
            personalization.addSubstitution("{{user}}", templateData.getString("user"));
            personalization.addSubstitution("{{equipment}}", templateData.getString("equipment"));
            personalization.addSubstitution("{{location}}", templateData.getString("location"));
            personalization.addSubstitution("{{size}}", templateData.getString("size"));

            StringBuilder topResultsBuilder = new StringBuilder();
            JSONArray topResultsArray = templateData.getJSONArray("topResults");
            for (int i = 0; i < topResultsArray.length(); i++) {
                JSONObject result = topResultsArray.getJSONObject(i);
                String title = result.getString("title");
                String product_link = result.getString("product_link");
                String imageUrl = result.getString("thumbnail"); // Retrieve the image URL if available

                topResultsBuilder.append("<div style='margin-bottom: 20px;'>")
                        .append("<a href=\"").append(product_link).append("\">")
                        .append("<img src='").append(imageUrl).append("' alt='Thumbnail' style='width:100px;height:auto;display:block;margin:auto;'>")
                        .append("<span style='display:block;text-align:center;margin-top:10px;'>").append(title).append("</span></a></div>");
            }

            personalization.addSubstitution("{{topResults}}", topResultsBuilder.toString());

            mail.addPersonalization(personalization);
            mail.setTemplateId("d2a18082-5804-41a3-bc2e-d60ade2632c2");

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

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                User user = userRepository.findByUsername(username);

                System.out.println("Number of filters due for change: " + filtersDueForChange.size());
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
            int count = 1;
            for (JsonNode result : shoppingResults) {
                if (count > 3) {
                    break; // Include only the top 3 results
                }
                String title = result.has("title") ? result.get("title").asText() : "No title";
                String product_link = result.has("product_link") ? result.get("product_link").asText() : "No link";
                String imageUrl = result.has("thumbnail") ? result.get("thumbnail").asText() : "No image";

                JSONObject resultObj = new JSONObject();
                resultObj.put("title", title);
                resultObj.put("product_link", product_link);
                resultObj.put("thumbnail", imageUrl);
                topResultsArray.put(resultObj);
                count++;
            }
        }

        jsonData.put("topResults", topResultsArray);
        System.out.println("Template Data: " + jsonData);

        // Construct the EmailRequest object
        EmailRequest emailRequest = new EmailRequest(
                filter.getEquipment().getId(),
                filter.getId(),
                user.getEmail(),
                "Filter Change Reminder",
                "Your filter for " + filter.getEquipment().getName() + " is due for change.",
                jsonData,
                new HashMap<>()
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

}


