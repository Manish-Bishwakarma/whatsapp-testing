import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SmartReplyResponse {
  suggestions: string[];
}

export interface SentimentResponse {
  sentiment: string;
  confidence: number;
}

export interface SummaryResponse {
  summary: string;
}

@Injectable({
  providedIn: 'root'
})
export class AiService {
  private apiUrl = 'http://localhost:8080/api/v1/ai';

  constructor(private http: HttpClient) {}

  getSmartReplies(message: string): Observable<SmartReplyResponse> {
    return this.http.post<SmartReplyResponse>(
      `${this.apiUrl}/smart-replies`,
      { message }
    );
  }

  analyzeSentiment(message: string): Observable<SentimentResponse> {
    return this.http.post<SentimentResponse>(
      `${this.apiUrl}/analyze-sentiment`,
      { message }
    );
  }

  summarizeChat(messages: string[]): Observable<SummaryResponse> {
    return this.http.post<SummaryResponse>(
      `${this.apiUrl}/summarize`,
      { messages }
    );
  }
}
