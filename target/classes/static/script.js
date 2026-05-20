<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Умная Теплица - Панель управления</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f7f6; margin: 0; padding: 20px; }
        .dashboard { max-width: 800px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }
        .header { text-align: center; color: #2c3e50; }
        .sensor-card { background: #e8f4f8; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 20px; }
        .temp-value { font-size: 48px; font-weight: bold; color: #e74c3c; }
    </style>
</head>
<body>

<div class="dashboard">
    <h1 class="header">Умная Теплица</h1>

    <div class="sensor-card">
        <h3>Текущая температура</h3>
        <div class="temp-value" id="currentTemp">-- °C</div>
        <p id="sensorStatus">Ожидание данных...</p>
    </div>

    <div>
        <canvas id="tempChart"></canvas>
    </div>
</div>

<script src="script.js"></script>
</body>
</html>