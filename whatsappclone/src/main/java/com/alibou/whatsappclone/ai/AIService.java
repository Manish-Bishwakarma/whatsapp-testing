package com.alibou.whatsappclone.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AIService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";

    public List<String> generateSmartReplies(String message) {
        try {
            String prompt = "You are a messaging assistant. Generate exactly 3 short, natural, and appropriate reply suggestions (maximum 10 words each) for this message: \"" + message + "\". Return ONLY the 3 replies, one per line, nothing else.";

            String response = callGeminiAPI(prompt);

            // Parse response and extract 3 replies
            String[] lines = response.split("\n");
            List<String> replies = new ArrayList<>();

            for (String line : lines) {
                String cleaned = line.trim().replaceAll("^[0-9]+\\.\\s*", "").replaceAll("^-\\s*", "");
                if (!cleaned.isEmpty() && replies.size() < 3) {
                    replies.add(cleaned);
                }
            }

            // Fallback if AI didn't return exactly 3
            while (replies.size() < 3) {
                replies.add("Thanks!");
            }

            return replies.subList(0, 3);

        } catch (Exception e) {
            log.error("Error generating smart replies", e);
            // Fallback responses
            return List.of("Thanks!", "Okay!", "Sure!");
        }
    }

    public SentimentResponse analyzeSentiment(String message) {
        try {
            String prompt = "Analyze the sentiment of this message and respond with ONLY ONE WORD - either 'positive', 'negative', or 'neutral': \"" + message + "\"";

            String response = callGeminiAPI(prompt);
            String sentiment = response.toLowerCase().trim();

            // Ensure valid sentiment
            if (!sentiment.matches("positive|negative|neutral")) {
                sentiment = "neutral";
            }

            // Calculate confidence (simplified for now)
            double confidence = 0.85;

            return new SentimentResponse(sentiment, confidence);

        } catch (Exception e) {
            log.error("Error analyzing sentiment", e);
            return new SentimentResponse("neutral", 0.0);
        }
    }

    public String summarizeMessages(List<String> messages) {
        try {
            String conversationText = String.join("\n", messages);
            String prompt = "Summarize this conversation in 2-3 sentences:\n" + conversationText;

            return callGeminiAPI(prompt);

        } catch (Exception e) {
            log.error("Error summarizing messages", e);
            return "Unable to generate summary at this time.";
        }
    }

    private String callGeminiAPI(String prompt) throws Exception {
        String url = GEMINI_API_URL + "?key=" + geminiApiKey;

        // Build request body
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        // Make request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Parse response
        JsonNode root = objectMapper.readTree(response.getBody());
        String text = root.path("candidates").get(0)
                .path("content").path("parts").get(0)
                .path("text").asText();

        return text;
    }
}