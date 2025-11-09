package com.alibou.whatsappclone.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentimentResponse {
    private String sentiment; // "positive", "negative", "neutral"
    private double confidence;
}