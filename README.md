# MarketIntelligenceTokens
Transform raw transactional artifacts into actionable market intelligence.
This project processes transaction data from CSV files following a specific format.

## Introduction and Methodology

This project was designed to address the evaluation criteria provided:

- **Part 1 – Data Structuring**: The main goal is to convert unstructured CSV data into a structured dataset. I chose CSV as the input format because it is the most commonly exported format from spreadsheets and other transactional systems. The application reads the CSV and converts each row into structured JSON objects, capturing information such as token counts per date, GMV, SKU, and Brand. These JSON objects allow both programmatic analysis and human interpretation.

- **Part 2 – Dataset Analysis**: Using the structured JSON, the system generates an exploratory web page where users can interpret data patterns, evaluate GMV distribution by brand, sales concentration (top SKUs vs long tail), average ticket and dispersion, and any other insights that can be inferred. Additionally, users can request AI-assisted analysis directly from the structured dataset.

- **Part 3 – Economic Value and Market Impact**: Beyond raw data analysis, the JSON dataset and web interface enable further evaluation of market trends, economic impact, and token-related insights.

This approach balances **accuracy, scalability, and cost** by leveraging a lightweight CSV input format, structured JSON processing, and optional AI-assisted interpretation.

## Repository

[https://github.com/RobertoMVB/MarketIntelligenceTokens](https://github.com/RobertoMVB/MarketIntelligenceTokens)

## Prerequisites

- Java 21 or higher
- Maven

## GEMINI API Key

Before running the project, you must create a properties file with your GEMINI API key.  
Request the key from Roberto (the repository owner).

### 1. Create the file at:
```bash
src/main/resources/application.properties
```
### 2. Add the following line, replacing `GEMINI_KEY` with the key provided:
```bash
ai.key=GEMINI_KEY
```
## Input CSV File

The input CSV file **must follow the format** of the sample file:

- SampleData/HYPR_Challenge_RMNF_FY26.csv


## How to Run

### 1. Clone the repository:

```bash
git clone https://github.com/RobertoMVB/MarketIntelligenceTokens.git
cd MarketIntelligenceTokens
```

### 2. Build the project and generate the JAR using Maven:
```bash
mvn clean package
```

### 3. Run the project by providing the path to the generated JAR and the CSV file:
```bash
java -jar target/market-intelligence-tokens-1.0-SNAPSHOT.jar PASTE_CSV_PATH_HERE
```

Replace PASTE_CSV_PATH_HERE with the full path to your input CSV file.
```bash
java -jar target/market-intelligence-tokens-1.0-SNAPSHOT.jar SampleData/HYPR_Challenge_RMNF_FY26.csv
```

Make sure the CSV file strictly **follows the exact format of the sample file**.

### 4. Open the generated web page for analysis:
After running the JAR, go to the generated folder and open the HTML page:
```bash
TransactionAnalysis/index.html
```
On this page, you can:

 - View the extracted structured data
 - Use the AI Analysis button to request further insights and interpretation of the dataset

This project transforms raw transactional data into structured JSON, enabling data interpretation, visualization in a web page, and AI-assisted analysis. It demonstrates the methodology used, trade-offs considered, and provides a reliable structured dataset ready for further analysis.
