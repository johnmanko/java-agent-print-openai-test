This is a sample project that utilizes a Java Agent to intercept `System.out` to inspect for Personally Identifiable Information (PII).  It makes use of OpenAI for inspection.

The instructions given to the model are:

> I want you to interpret the content of text that I'll provide below.  Please indicate if the text contains any personal identifiable information (PII), such as credit card number, name, address, social security number ("ssn"), or phone number, email.
> 
> Return your response in JSON format, where the format is the following:
> 
> {
> 
>    "severity": Can be "LOW" (no PII found), "MEDIUM" (name, address, email or phone number is found) or "HIGH" (when credit card or social security number is found),
> 
>    "type": string[] (a string array of the types of PII found: "name", "address", "phone", "email", "credit_card", "ssn")
> 
>    "description": A description of what your found,
> 
>    "original_text": The original text analyzed during this process
> 
> }
> 
> Here is the text: %s

Expected json format:
```json
{
   "severity": string, // "LOW", "MEDIUM", or "HIGH"
   "type": string[], // "name", "address", "phone", "email", "credit_card", "ssn")
   "description": string,
   "original_text": string,
}
```

Copy `.env-example` to `.env` and edit to include your OpenAI API Key:

```shell
cp .env-example .env
```

Compile the project:
```shell
./mvnw dependency:resolve
./mvnw clean package
```

Run the demo:
```shell
export $(grep -v '^#' .env | xargs) 
java -javaagent:target/java-agent-print-openai-test-1.0-SNAPSHOT.jar -jar target/java-agent-print-openai-test-1.0-SNAPSHOT.jar
```

Test text:

```json
{
"severity": "LOW",
"type": [],
"description": "No personal identifiable information (PII) found in the text.",
"original_text": "Hello, World!"
}
```

```json
{
"severity": "MEDIUM",
"type": ["name"],
"description": "The output contains a customer's name, which is personal identifiable information.",
"original_text": "Order #341234, Customer: Jackie Lewis"
}
```

```json
{
"severity": "HIGH",
"type": ["credit_card"],
"description": "The output contains a credit card number in the format of a VISA card, including the expiration date.",
"original_text": "VISA 9874-4568-1354-9854 01/2027 444"
}
```

```json
{
"severity": "HIGH",
"type": ["credit_card"],
"description": "The output contains a credit card number, including its expiration date and security code.",
"original_text": "CC Charged successful!  4488-4568-8854-4545 04/2029 951"
}
```

```json
{
"severity": "MEDIUM",
"type": ["phone"],
"description": "The output text contains a phone number.",
"original_text": "SMS sent! 1-987-555-8547: Your order has shipped!"
}
```

```json
{
"severity": "HIGH",
"type": ["ssn"],
"description": "The output contains a social security number in the format '987-65-4321'.",
"original_text": "Id verified: 987-65-4321!"
}
```

```json
{
"severity": "LOW",
"type": [],
"description": "No personal identifiable information (PII) found in the output text. The message indicates a technical error related to customer details but does not provide any specific identifiable information.",
"original_text": "NullPointerException - Can't save customer details - ID 1448446!"
}
```

```json
{
"severity": "LOW",
"type": [],
"description": "No personal identifiable information (PII) found in the text.",
"original_text": "Call Inventory Service"
}
```

```json
{
"severity": "MEDIUM",
"type": ["email", "phone"],
"description": "The output contains an email address (jimmy@example.com) and a phone number (212-258-4584).",
"original_text": "Notify client: jimmy@example.com; message: I can't find that item in our stock.  Would you consider an alternative?  I have item 212-258-4584 if you're interested."
}
```
