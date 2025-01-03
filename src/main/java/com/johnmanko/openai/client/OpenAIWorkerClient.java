package com.johnmanko.openai.client;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionMessageParam;
import com.openai.models.ChatCompletionUserMessageParam;
import com.openai.models.ChatModel;
import java.util.List;

public class OpenAIWorkerClient {

    public String requestInference(String output, final String API_KEY) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(API_KEY)
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .messages(List.of(ChatCompletionMessageParam.ofChatCompletionUserMessageParam(ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.USER)
                        .content(ChatCompletionUserMessageParam.Content.ofTextContent(
                                    String.format("""
                                            I have a program that's outputting the following text to the console. 
                                            I want you to interpret the content of the output text.  Please indicate 
                                            if the output text contains any personal identifiable information (PII), such as
                                            credit card number, name, address, social security number ("ssn"), or phone number, email.
                                            Return your response in JSON format, where the format is the following:
                                            {
                                               "severity": Can be "LOW" (no PII found), "MEDIUM" (name, address, email or phone number is found) or "HIGH" (when credit card or social security number is found), 
                                               "type": string[] (a string array of the types of PII found: "name", "address", "phone", "email", "credit_card", "ssn"),
                                               "description": A description of what your found,
                                               "original_text": The original text analyzed during this process
                                            }
                                            Here is the text: %s
                                            """,
                                            output)
                                )
                        )
                        .build())))
                .model(ChatModel.GPT_4O_MINI)
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        return chatCompletion.choices().getFirst().message().content().orElse("{}");

    }

}
