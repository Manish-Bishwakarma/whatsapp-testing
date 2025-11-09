package com.alibou.whatsappclone.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIController {

    private final AIService aiService;

    @PostMapping("/smart-replies")
    public ResponseEntity<SmartReplyResponse> generateSmartReplies(
            @RequestBody SmartReplyRequest request
    ) {
        List<String> replies = aiService.generateSmartReplies(request.getMessage());
        SmartReplyResponse response = new SmartReplyResponse(replies);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<SentimentResponse> analyzeSentiment(
            @RequestBody SentimentRequest request
    ) {
        SentimentResponse sentiment = aiService.analyzeSentiment(request.getMessage());
        return ResponseEntity.ok(sentiment);
    }

    @PostMapping("/summarize")
    public ResponseEntity<SummaryResponse> summarizeChat(
            @RequestBody SummarizeRequest request
    ) {
        String summary = aiService.summarizeMessages(request.getMessages());
        SummaryResponse response = new SummaryResponse(summary);
        return ResponseEntity.ok(response);
    }
}