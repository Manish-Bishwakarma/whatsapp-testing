package com.alibou.whatsappclone.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SmartReplyResponse {
    private List<String> suggestions;
}