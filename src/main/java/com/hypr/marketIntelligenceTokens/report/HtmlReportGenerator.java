package com.hypr.marketIntelligenceTokens.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HtmlReportGenerator {

    public static void generate(Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve("index.html"), html());
        System.out.println("HTML report gerado com gráficos.");
    }

    private static String html() {
        return """
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>Transaction Analysis</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    font-family: Arial, sans-serif;
    background: #f4f6f8;
    margin: 32px;
}
.card {
    background: white;
    padding: 20px;
    margin-bottom: 24px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,.08);
}
h1, h2 {
    margin-top: 0;
}
canvas {
    max-height: 300px;
}
table {
    width: 100%;
    border-collapse: collapse;
}
th, td {
    padding: 8px;
    border-bottom: 1px solid #ddd;
}
th {
    background: #eee;
}
</style>
</head>

<body>

<h1>Transaction Analysis Report</h1>

<div class="card">
    <h2>Overview</h2>
    <pre id="overview"></pre>
</div>

<div class="card">
    <h2>GMV por Brand</h2>
    <canvas id="brandChart"></canvas>
    <br>
    <table>
        <thead>
        <tr>
            <th>Brand</th>
            <th>GMV</th>
            <th>Share (%)</th>
        </tr>
        </thead>
        <tbody id="brandTable"></tbody>
    </table>
</div>

<div class="card">
    <h2>GMV por Dia</h2>
    <canvas id="dailyChart"></canvas>
</div>

<script>
async function loadJson(file) {
    const res = await fetch(file);
    return res.json();
}

async function renderOverview() {
    const data = await loadJson('overview.json');
    document.getElementById('overview').textContent =
        JSON.stringify(data, null, 2);
}

async function renderBrandGmv() {
    const data = await loadJson('brand_gmv.json');

    const labels = data.brands.map(b => 'Brand ' + b.brand);
    const values = data.brands.map(b => b.gmv);

    new Chart(document.getElementById('brandChart'), {
        type: 'bar',
        data: {
            labels,
            datasets: [{
                label: 'GMV',
                data: values
            }]
        }
    });

    const tbody = document.getElementById('brandTable');
    data.brands.forEach(b => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${b.brand}</td>
            <td>${b.gmv.toFixed(2)}</td>
            <td>${b.sharePercent.toFixed(2)}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function renderDaily() {
    const data = await loadJson('daily_brand_overview.json');

    const dates = Object.keys(data);
    const totals = dates.map(d =>
        Object.values(data[d]).reduce((a, b) => a + b, 0)
    );

    new Chart(document.getElementById('dailyChart'), {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: 'GMV Diário',
                data: totals,
                fill: false
            }]
        }
    });
}

async function init() {
    await renderOverview();
    await renderBrandGmv();
    await renderDaily();
}

init();
</script>

</body>
</html>
""";
    }

    private HtmlReportGenerator() {}
}
