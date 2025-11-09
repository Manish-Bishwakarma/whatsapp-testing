package com.alibou.whatsappclone.ai;

import lombok.Data;
import java.util.List;

@Data
public class SummarizeRequest {
    private List<String> messages;
}