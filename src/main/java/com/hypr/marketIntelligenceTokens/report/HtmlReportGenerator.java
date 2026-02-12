package com.hypr.marketIntelligenceTokens.report;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class HtmlReportGenerator {

    private static String apiKey;

    static {
        Properties props = new Properties();
        try (InputStream in = HtmlReportGenerator.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) throw new RuntimeException("application.properties NOT FOUND !" +
                    " Have you asked the properties files for Roberto?");
            props.load(in);
            apiKey = props.getProperty("ai.key");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generate(Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve("index.html"), html(apiKey));
        System.out.println("HTML report gerado com gráficos e botão de avaliação com IA.");
    }

    private static String html(String apiKey) {
        return String.format("""
<!DOCTYPE html>
<html lang="pt-BR">
<head>
<meta charset="UTF-8">
<title>Market Intelligence Report - Gemini AI</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
    body { font-family: Arial, sans-serif; background: #f5f6fa; margin: 20px; color: #333; }
    h1 { margin-bottom: 5px; }
    .subtitle { color: #666; margin-bottom: 20px; }
    .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 15px; margin-bottom: 30px; }
    .card { background: white; padding: 15px; border-radius: 6px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
    .card h3 { margin: 0; font-size: 14px; color: #777; }
    .card p { margin: 5px 0 0; font-size: 22px; font-weight: bold; }
    .section { background: white; padding: 20px; border-radius: 6px; margin-bottom: 30px; }
    table { width: 100%%; border-collapse: collapse; margin-top: 15px; }
    th, td { padding: 8px; border-bottom: 1px solid #eee; text-align: right; }
    th { background: #fafafa; }
    th:first-child, td:first-child { text-align: left; }
    button#evaluateAI { background: #1a73e8; color: white; border: none; padding: 12px 20px; border-radius: 4px; cursor: pointer; margin-bottom: 20px; font-weight: bold; font-size: 16px; }
    button#evaluateAI:hover { background: #1557b0; }
    button#evaluateAI:disabled { background: #ccc; cursor: not-allowed; }
    #aiResponse { 
        background: #ffffff; padding: 20px; border-radius: 8px; border-left: 6px solid #1a73e8; 
        margin-bottom: 30px; display: none; white-space: pre-wrap; line-height: 1.6;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    .loading-spinner { display: inline-block; width: 12px; height: 12px; border: 2px solid #ffffff; border-radius: 50%%; border-top-color: transparent; animation: spin 1s linear infinite; margin-right: 8px; }
    @keyframes spin { to { transform: rotate(360deg); } }
</style>
</head>
<body>

<h1>Market Intelligence</h1>
<div class="subtitle">Relatório Consolidado — Inteligência Artificial Aplicada</div>

<div style="text-align: center;">
    <button id="evaluateAI">Avaliar com Gemini AI</button>
</div>

<div id="aiResponse"></div>

<div class="cards" id="cards"></div>

<div class="section">
    <h2>GMV por Brand</h2>
    <canvas id="brandChart"></canvas>
</div>

<div class="section">
    <h2>GMV por Dia</h2>
    <canvas id="dailyChart"></canvas>
</div>

<div class="section">
    <h2>Detalhamento Diário</h2>
    <table id="dailyTable">
        <thead>
            <tr>
                <th>Data</th>
                <th>Transações</th>
                <th>Completas</th>
                <th>Incompletas</th>
                <th>GMV</th>
            </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<script>
const state = { overview: null, sku: null, brand: null, daily: null };

// --- CONFIGURAÇÃO ---
const GEMINI_API_KEY = '%s';

async function loadJson(name) {
    try {
        const res = await fetch(name);
        return await res.json();
    } catch (e) {
        console.warn("Aviso: Arquivo " + name + " não encontrado.");
        return null;
    }
}

async function init() {
    state.overview = await loadJson('overview.json');
    state.sku = await loadJson('sku_concentration.json');
    state.brand = await loadJson('brand_gmv.json');
    state.daily = await loadJson('daily_brand_overview.json');
    render();
}

function render() {
    renderCards();
    renderBrandChart();
    renderDailyChart();
    renderDailyTable();
}

function renderCards() {
    const c = document.getElementById('cards');
    c.innerHTML = '';
    if (state.overview) {
        c.innerHTML += '<div class="card"><h3>Total GMV</h3><p>' + state.overview.totalGmv.toFixed(2) + '</p></div>';
        c.innerHTML += '<div class="card"><h3>Transações</h3><p>' + state.overview.totalTransactions + '</p></div>';
    }
    if (state.sku) {
        c.innerHTML += '<div class="card"><h3>SKUs Totais</h3><p>' + state.sku.totalSkus + '</p></div>';
    }
}

function renderBrandChart() {
    if (!state.brand) return;
    const labels = state.brand.brands.map(b => "Marca " + b.brand);
    const data = state.brand.brands.map(b => b.gmv);
    new Chart(document.getElementById('brandChart'), { 
        type: 'bar', 
        data: { labels, datasets: [{ label: 'GMV por Marca', data, backgroundColor: '#4b7bec' }] } 
    });
}

function renderDailyChart() {
    if (!state.daily) return;
    const dates = Object.keys(state.daily).sort();
    const gmvs = dates.map(d =>
        Object.values(state.daily[d].brands).reduce((a, b) => a + (b.gmv || 0), 0)
    );
    new Chart(document.getElementById('dailyChart'), { 
        type: 'line', 
        data: { labels: dates, datasets: [{ label: 'Performance Diária', data: gmvs, borderColor: '#1a73e8', tension: 0.1 }] } 
    });
}

function renderDailyTable() {
    if (!state.daily) return;
    const tbody = document.querySelector('#dailyTable tbody');
    tbody.innerHTML = '';
    Object.entries(state.daily).sort().forEach(([date, d]) => {
        const gmv = Object.values(d.brands).reduce((a, b) => a + (b.gmv || 0), 0);
        const row = '<tr>' +
            '<td>' + date + '</td>' +
            '<td>' + d.totalTransactions + '</td>' +
            '<td>' + d.completeTransactions + '</td>' +
            '<td>' + d.incompleteTransactions + '</td>' +
            '<td>' + gmv.toFixed(2) + '</td>' +
            '</tr>';
        tbody.innerHTML += row;
    });
}

document.getElementById('evaluateAI').addEventListener('click', async () => {
    const btn = document.getElementById('evaluateAI');
    const responseDiv = document.getElementById('aiResponse');
    btn.innerHTML = '<span class="loading-spinner"></span> Analisando Dados...';
    btn.disabled = true;

    const promptText = "Você como um expert de avaliação de GMV por marcas, avalie os seguintes dados e forneça insights sobre tendências e saúde das transações: " + JSON.stringify(state);
    const url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;

    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ contents: [{ parts: [{ text: promptText }] }] })
        });

        const data = await res.json();
        if (data.candidates && data.candidates[0].content) {
            const resultText = data.candidates[0].content.parts[0].text;
            responseDiv.style.display = "block";
            responseDiv.innerText = "ANÁLISE DO ESPECIALISTA:\\n\\n" + resultText;
            window.scrollTo({ top: 0, behavior: 'smooth' });
        } else {
            throw new Error(data.error ? data.error.message : "Erro na resposta da IA");
        }
    } catch (e) {
        console.error(e);
        alert("Erro ao consultar Gemini: " + e.message);
    } finally {
        btn.innerText = "Avaliar com Gemini AI";
        btn.disabled = false;
    }
});

init();
</script>
</body>
</html>
""", apiKey);
    }

    private HtmlReportGenerator() {}
}
